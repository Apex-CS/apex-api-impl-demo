package com.fis.api.product.BPPRetrieveProductDetail.gateways.v1.get;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface ProductGateway {

    @Gateway(requestChannel = "product.get.gateway.channel")
    public String sendMessage(String message);
}
