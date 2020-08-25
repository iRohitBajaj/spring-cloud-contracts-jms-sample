package com.example;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BarTenderApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = { "destination.drinkrequest=req.q" })
@AutoConfigureMessageVerifier
@ActiveProfiles("test")
public abstract class BeerMessagingBase {
	@Inject
    MessageVerifier messaging;

	@Before
	public void setup() {
		this.messaging.receive("req.q", 100, TimeUnit.MILLISECONDS);
	}
}

