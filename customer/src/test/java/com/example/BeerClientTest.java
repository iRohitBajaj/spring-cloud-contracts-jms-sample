package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.example:bartender:1.0-SNAPSHOT")
@ActiveProfiles("test")
public class BeerClientTest {

    @StubRunnerPort("bartender") int producerPort;

    @Test
    public void should_give_me_a_beer_when_im_old_enough() {
        BeerResponse response = new RestTemplate()
                .postForObject("http://localhost:" + this.producerPort + "/",
                        new BeerRequest("jack", Long.valueOf(22)), BeerResponse.class);

        BDDAssertions.then(response.isEligible()).isEqualTo(true);
    }

    @Test public void should_reject_a_beer_when_im_too_young() {
        BeerResponse response = new RestTemplate()
                .postForObject("http://localhost:" + this.producerPort + "/",
                        new BeerRequest("Alex", Long.valueOf(15)), BeerResponse.class);

        BDDAssertions.then(response.isEligible()).isEqualTo(false);
    }
}
