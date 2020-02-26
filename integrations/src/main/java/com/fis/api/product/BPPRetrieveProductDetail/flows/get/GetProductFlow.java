package com.fis.api.product.BPPRetrieveProductDetail.flows.get;

import com.fis.api.product.BPPRetrieveProductDetail.*;
import com.fis.api.product.BPPRetrieveProductDetail.gateways.v1.get.GetProductGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.*;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.fis.api.product.BPPRetrieveProductDetail.ProductIntegrationConstants.*;
import static com.fis.api.product.BPPRetrieveProductDetail.flows.ProductFlow.standardGateway;
import static com.fis.api.product.BPPRetrieveProductDetail.gateways.v1.get.GetProductGateway.PRODUCT_GATEWAY_CHANNEL;

@Component
@MessageEndpoint
public class GetProductFlow extends IntegrationFlowAdapter implements MessageProcessor {
    private Logger logger = LoggerFactory.getLogger(GetProductFlow.class);
    //Xslts
    @Value("${:classpath:/mapping/product-1.0/getProduct/xslt/"
            + "GetProductRequest.xslt}")
    private Resource requestXslt;
    @Value("${:classpath:/mapping/product-1.0/getProduct/xslt/"
            + "GetProductResponse.xslt}")
    private Resource responseXslt;

    //Autowired Properties
    @Autowired
    private FiservDnaResponseVerifier fiservDnaResponseVerifier;

    private MessageChannel outputChannel;
    private MessageChannel errorChannel;

    @Autowired
    private FisDynamicHostDestinationProvider fisDynamicHostProvider;

    @Autowired
    private ProductAPI productAPI;

    private int productId;
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

        MessagingGatewaySupport inboundGateway = productAPI.getProductById( productId );
        errorChannel = inboundGateway.getErrorChannel();
        outputChannel = inboundGateway.getReplyChannel();

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

    public MessageChannel getOutputChannel() {
        return outputChannel;
    }

    public void setOutputChannel(MessageChannel outputChannel) {
        this.outputChannel = outputChannel;
    }

    public MessageChannel getErrorChannel() {
        return errorChannel;
    }

    public void setErrorChannel(MessageChannel errorChannel) {
        this.errorChannel = errorChannel;
    }

    @ServiceActivator(inputChannel = PRODUCT_GATEWAY_CHANNEL)
    public Object processMessage(Message message) {
        this.productId = Integer.parseInt(message.getPayload().toString());
        logger.info(" ProductId : "+ this.productId);
        return message;
    }
}