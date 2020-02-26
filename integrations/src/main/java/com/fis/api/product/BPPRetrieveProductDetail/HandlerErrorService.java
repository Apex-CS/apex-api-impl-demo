package com.fis.api.product.BPPRetrieveProductDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class HandlerErrorService implements MessageProcessor {

        private Logger logger = LoggerFactory.getLogger(HandlerErrorService.class);

        @Override
        public Object processMessage(Message message) {
                logger.info(message.getPayload().toString());
                return message;
        }
}
