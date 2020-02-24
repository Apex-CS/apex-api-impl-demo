package com.fis.api.product.BPPRetrieveProductDetail.flows.get;

import com.fis.api.product.BPPRetrieveProductDetail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.integration.dsl.*;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.fis.api.product.BPPRetrieveProductDetail.ProductIntegrationConstants.*;
import static com.fis.api.product.BPPRetrieveProductDetail.flows.ProductFlow.standardGateway;

@Component
public class GetProductFlow extends IntegrationFlowAdapter {

    //Xslts
    @Value("${:classpath:/mapping/product-1.0/getProduct/xslt/"
            + "GetProductRequest.xslt}")
    private Resource requestXslt;
    @Value("${:classpath:/mapping/product-1.0/getProduct/xslt/"
            + "GetProductResponse.xslt}")
    private Resource responseXslt;

    //Autowired Properties
    @Qualifier(RelationshipIntegrationConstants.FISERV_RESPONSE_VERIFIER_BEAN_NAME)
    private FiservDnaResponseVerifier fiservDnaResponseVerifier;
    @Autowired
    @Qualifier(MessageOnlyErrorFlow.ERROR_CHANNEL_BEAN_NAME)
    private MessageChannel errorChannel;
    @Autowired
    @Qualifier(RelationshipIntegrationConstants.RELATIONSHIP_HOST_DESTINATION_PROVIDER)
    private FisDynamicHostDestinationProvider fisDynamicHostProvider;

    @Autowired
    ProductAPI productAPI;

    private String classMethodName = getClass().getName();

    //endpointConfigurer
    private Consumer<GatewayEndpointSpec> endpointConfigurer = gwSpec ->
            gwSpec.errorChannel(errorChannel);

    //ParamHeaders
    private static String[] PARAM_HEADERS =
            {
                    UUID_HEADERNAME,
                    APPLICATION_ID_HEADERNAME,
                    ORGANIZATION_ID_HEADERNAME,
                    CUSTOMER_ID
            };

    @Override
    protected IntegrationFlowDefinition<?> buildFlow() {
        MessagingGatewaySupport inboundGateway = productAPI.getProductById();
        inboundGateway.setErrorChannel(errorChannel);

        inboundGateway.setReplyTimeout(RelationshipIntegrationConstants.DEFAULT_REPLY_TIMEOUT);
        return IntegrationFlows
                .from(inboundGateway)
                .gateway(buildMainFlow());
    }

    public IntegrationFlow buildMainFlow() {
        return flow -> flow
                .gateway(standardGateway(
                        requestXslt,
                        responseXslt,
                        fisDynamicHostProvider,
                        fiservDnaResponseVerifier,
                        PARAM_HEADERS, classMethodName),
                        endpointConfigurer);
    }

}