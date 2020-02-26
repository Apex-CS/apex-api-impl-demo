package com.fis.api.product.BPPRetrieveProductDetail.gateways.v1.get;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface GetProductGateway {

    public static String PRODUCT_GATEWAY_CHANNEL = "product.get.gateway.channel";

    @Gateway(requestChannel = PRODUCT_GATEWAY_CHANNEL)
    public String sendMessage(String message);
}
