package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    public void namesFlux() {
        // given

        // when
        final var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        // then
        StepVerifier.create(namesFlux)
//                .expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void namesMono() {
        var nameMono = fluxAndMonoGeneratorService.nameMono();

        StepVerifier.create(nameMono)
                .expectNext("carl")
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        var namesFluxMap = fluxAndMonoGeneratorService.namesFluxMap();
        StepVerifier.create(namesFluxMap)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }
}
