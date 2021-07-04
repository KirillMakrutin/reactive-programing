package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoSchedulersServiceTest {

    private final FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

    @Test
    void explorePublishOn() {
        var stringFlux = fluxAndMonoSchedulersService.explorePublishOn();
        StepVerifier.create(stringFlux)
                .expectNextCount(6)
                .verifyComplete();
    }
}