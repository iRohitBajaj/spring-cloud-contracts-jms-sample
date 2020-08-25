
package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Queue;
import javax.jms.Session;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.example:bartender:1.0-SNAPSHOT"
        ,properties = { "destination.drinkrequest=req2.q" }
)
@ActiveProfiles("test")
public class BeerConsumerAllowTest {

    @Autowired
    StubTrigger stubTrigger;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired BeerConsumer beerConsumer;
    @Test
    public void should_allow_beer_request() throws Exception {
        Session session = jmsTemplate.getConnectionFactory().createConnection()
                .createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue =session.createQueue("resp2.q");
        this.stubTrigger.trigger("allow_beer");
        then(this.beerConsumer.receiveBeer("allowMessage", queue).isEligible()).isEqualTo(true);
    }
}
