package com.fis.api.product.BPPRetrieveProductDetail.gateways.v1.create;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface ProductGateway {

    @Gateway
    public String sendMessage(String message);
}
