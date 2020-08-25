package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

@RestController
public class BeerController {
    public BeerController(BeerConsumer beerConsumer, JmsTemplate jmsTemplate) {
        this.beerConsumer = beerConsumer;
        this.jmsTemplate = jmsTemplate;
    }

    @Value("${destination.drinkresponse}")
    private String beerResponseDestination;

    BeerConsumer beerConsumer;
    JmsTemplate jmsTemplate;

    @PostMapping("/")
    public ResponseEntity<BeerResponse> askForBeer(@RequestBody BeerRequest beerRequest) throws JMSException {
        Session session = jmsTemplate.getConnectionFactory().createConnection()
                .createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue responseDest = session.createQueue(beerResponseDestination);
        String messageId = beerConsumer.askForBeer(beerRequest, responseDest);
        return ResponseEntity.ok(beerConsumer.receiveBeer(messageId, responseDest));
    }

}
