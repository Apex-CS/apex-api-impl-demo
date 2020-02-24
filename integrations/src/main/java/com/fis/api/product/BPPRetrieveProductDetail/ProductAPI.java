package com.fis.api.product.BPPRetrieveProductDetail;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.stereotype.Component;

@Component
public class ProductAPI {
    private MessagingGatewaySupport messagingGateway;

    public MessagingGatewaySupport getProductById() {

        this.messagingGateway = new MessagingGatewaySupport() {
        };

        return this.messagingGateway;
    }
}
