package com.example;

import org.springframework.stereotype.Component;

@Component
public class BeerService {
    public BeerResponse isEligibleForBeer(BeerRequest beerRequest) {
        return new BeerResponse(beerRequest.getAge() > 19);
    }
}
