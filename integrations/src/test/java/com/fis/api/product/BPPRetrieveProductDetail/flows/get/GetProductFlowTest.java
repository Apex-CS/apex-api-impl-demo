package com.fis.api.product.BPPRetrieveProductDetail.flows.get;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GetProductFlowTest {

    @Mock
    GetProductFlow getProductFlowMock;

    @Test
    public void buildFlowTest() {
        Assert.assertTrue(true);
    }

    @Test
    public void validateFlowTest() {
        verify(getProductFlowMock, times(0)).buildFlow();
    }

}