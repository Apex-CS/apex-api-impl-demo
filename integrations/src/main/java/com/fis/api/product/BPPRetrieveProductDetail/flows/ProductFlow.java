package com.fis.api.product.BPPRetrieveProductDetail.flows;

import com.fis.api.product.BPPRetrieveProductDetail.FisDynamicHostDestinationProvider;
import com.fis.api.product.BPPRetrieveProductDetail.FiservDnaResponseVerifier;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

public class ProductFlow {

    public static MessageChannel standardGateway (Resource request,
                                           Resource response,
                                           FisDynamicHostDestinationProvider fisDynamicHostDestinationProvider,
                                           FiservDnaResponseVerifier fiservDnaResponseVerifier,
                                           String[] param,
                                           String classMethodName){

        return new DirectChannel();
    }
}
