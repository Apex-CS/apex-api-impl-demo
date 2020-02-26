package com.fis.api.product.BPPRetrieveProductDetail;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ProductAPI implements ApplicationContextAware {
    private MessagingGatewaySupport messagingGateway;

    @Autowired
    HandlerErrorService handlerErrorService;
    private ErrorMessage errorMessage;

    public MessagingGatewaySupport getProductById(String productId) {
        DirectChannel reqChannel = new DirectChannel();
        int iProductId = 0;

        this.messagingGateway = new MessagingGatewaySupport() {
        };
        this.messagingGateway.setBeanFactory(ctx);

        try{
            iProductId = Integer.parseInt(productId);
        }catch(NumberFormatException ex){
            sendError(MessageOnlyErrorFlow.ERROR_MAPPING.get("PRODAPI-VAL-0019"));
        }

        if (iProductId < 100) {
            // do nothing
        } else {
            sendError(MessageOnlyErrorFlow.ERROR_MAPPING.get("PRODAPI-SYS-0001"));
        }

        this.messagingGateway.setRequestChannel(reqChannel);
        this.messagingGateway.setReplyChannel(null);
        this.messagingGateway.afterPropertiesSet();

        return this.messagingGateway;
    }

    private void sendError(String error){
        DirectChannel outChannel = new DirectChannel();
        PublishSubscribeChannel errorChannel = new PublishSubscribeChannel();
        List<Message> theSubscriberReceivedMessages =
                new CopyOnWriteArrayList<>();
        ServiceActivatingHandler handler = new ServiceActivatingHandler(handlerErrorService);
        handler.setBeanFactory(ctx);
        handler.setRequiresReply(false);
        handler.setOutputChannel(outChannel);
        errorChannel.subscribe(handler);

        MessageHandler theSubscriber = theSubscriberReceivedMessages::add;
        outChannel.subscribe(theSubscriber);
        this.messagingGateway.setErrorChannel(errorChannel);
        errorMessage = new ErrorMessage(new Exception(error));
        this.messagingGateway.getErrorChannel()
                .send(errorMessage);
    }

    private static ApplicationContext ctx = null;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    @Override
    public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
        ProductAPI.ctx = ctx;
    }


    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }
}
