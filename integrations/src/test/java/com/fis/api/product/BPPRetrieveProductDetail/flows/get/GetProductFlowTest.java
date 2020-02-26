package com.fis.api.product.BPPRetrieveProductDetail.flows.get;

import com.fis.api.product.BPPRetrieveProductDetail.gateways.v1.get.GetProductGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class GetProductFlowTest {

    @Autowired
    GetProductFlow getProductFlow;

    @Autowired
    GetProductGateway getProductGateway;

    @Test
    public void buildFlowTest() {
        Assert.isTrue(getProductFlow != null, "ProductFlow not created");
    }

    @Test
    public void integrationTest(){
        getProductGateway.sendMessage("105");
        getProductFlow.buildFlow();
    }

}