package com.example;

import java.util.concurrent.atomic.AtomicReference;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class BeerConsumer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BeerConsumer.class);

    private JmsTemplate jmsTemplate;

    public BeerConsumer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public String askForBeer(BeerRequest beerRequest, Queue beerResponseDestination) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(beerRequest, messagePostProcessor -> {
            LOGGER.info("setting standard JMS headers before sending");
            message.set(messagePostProcessor);
            messagePostProcessor.setJMSReplyTo(beerResponseDestination);
            return messagePostProcessor;
        });

        // used as correlation id
        String messageId = message.get().getJMSMessageID();
        LOGGER.info("sending beer request ='{}' with MessageId='{}'",
                beerRequest, messageId);

        return messageId;
    }

    public BeerResponse receiveBeer(String correlationId, Queue beerResponseDestination) {
        BeerResponse beerResponse = (BeerResponse) jmsTemplate.receiveSelectedAndConvert(
                beerResponseDestination,
                "jms_correlationId = '" + correlationId + "'");
        LOGGER.info("receive Status='{}' for CorrelationId='{}'", beerResponse,
                correlationId);

        return beerResponse;
    }
}
