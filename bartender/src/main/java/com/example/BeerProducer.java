package com.example;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;


@Component
public class BeerProducer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BeerProducer.class);

    private JmsTemplate jmsTemplate;
    BeerService beerService;
    MessageConverter messageConverter;

    public BeerProducer(JmsTemplate jmsTemplate,
                        BeerService beerService,
                        MessageConverter messageConverter) {
        this.jmsTemplate = jmsTemplate;
        this.beerService = beerService;
        this.messageConverter = messageConverter;
    }

    @JmsListener(destination = "${destination.drinkrequest}")
    public void receiveBeerRequests(BeerRequest beerRequest,
                                    Session session,
                             @Header(JmsHeaders.MESSAGE_ID) String messageId,
                                    @Header(JmsHeaders.REPLY_TO) String replyDestination) throws JMSException {
        LOGGER.info("received Beer request ='{}' with MessageId='{}'",
                beerRequest, messageId);

        LOGGER.info("sending Status with CorrelationId='{}'",
                messageId);
        LOGGER.info("sending Status to reply destination {}",
                replyDestination);
        BeerResponse beerResponse = beerService.isEligibleForBeer(beerRequest);
        jmsTemplate.convertAndSend(
                session.createQueue(replyDestination.replace("queue://","")),
                beerResponse, m -> {

            LOGGER.info("setting standard JMS headers before sending");
            m.setJMSCorrelationID(messageId);
            m.setStringProperty("jms_correlationId", messageId);
            return m;
        });
    }

}
