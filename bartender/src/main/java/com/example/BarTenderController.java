package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

@RestController
public class BarTenderController {
    BeerService beerService;

    public BarTenderController(BeerService beerService) {
        this.beerService = beerService;
    }

    @PostMapping("/")
    public ResponseEntity<BeerResponse> askForBeer(@RequestBody BeerRequest beerRequest) throws JMSException {
        return ResponseEntity.ok(beerService.isEligibleForBeer(beerRequest));
    }
}
