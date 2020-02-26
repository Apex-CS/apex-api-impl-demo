package com.fis.api.product.BPPRetrieveProductDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ProductAPI implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(HandlerErrorService.class);
    private MessagingGatewaySupport messagingGateway;

    @Autowired
    HandlerErrorService handlerErrorService;

    public MessagingGatewaySupport getProductById(int productId) {
        DirectChannel reqChannel = new DirectChannel();
        DirectChannel outChannel = new DirectChannel();

        List<Message> theSubscriberReceivedMessages =
                new CopyOnWriteArrayList<>();

        this.messagingGateway = new MessagingGatewaySupport() {
        };

        this.messagingGateway.setBeanFactory(ctx);

        if (productId < 100) {
            // do nothing
        } else {
            reqChannel.subscribe(message -> {
                throw new RuntimeException( "");
            });

            PublishSubscribeChannel errorChannel = new PublishSubscribeChannel();
            ServiceActivatingHandler handler = new ServiceActivatingHandler(handlerErrorService);
            handler.setBeanFactory(ctx);
            handler.setRequiresReply(false);
            handler.setOutputChannel(outChannel);
            errorChannel.subscribe(handler);

            MessageHandler theSubscriber = theSubscriberReceivedMessages::add;
            outChannel.subscribe(theSubscriber);
            this.messagingGateway.setErrorChannel(errorChannel);
            this.messagingGateway.getErrorChannel()
                    .send(new ErrorMessage(new Exception(MessageOnlyErrorFlow.ERROR_MAPPING.get("PRODAPI-SYS-0001"))));
        }

        this.messagingGateway.setRequestChannel(reqChannel);
        this.messagingGateway.setReplyChannel(outChannel);
        this.messagingGateway.afterPropertiesSet();

        return this.messagingGateway;
    }

    private static ApplicationContext ctx = null;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    @Override
    public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
        ProductAPI.ctx = ctx;
    }


}
