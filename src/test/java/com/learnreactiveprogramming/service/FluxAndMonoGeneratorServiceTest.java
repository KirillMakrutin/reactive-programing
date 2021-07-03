package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

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

    @Test
    void namesFluxImmutability() {
        var flux = fluxAndMonoGeneratorService.namesFluxImmutability();
        StepVerifier.create(flux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxFilter() {
        var flux = fluxAndMonoGeneratorService.namesFluxFilter(4);

        StepVerifier.create(flux)
                .expectNext("CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFluxFlatMap("ALEX"))
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFluxFlatMapAsync("ALEX", "BOB"))
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapSync() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFluxFlatMapSync("ALEX", "BOB"))
                .expectNext("A", "L", "E", "X", "B", "O", "B")
                .verifyComplete();
    }

    @Test
    void nameCharsMono() {

        StepVerifier.create(fluxAndMonoGeneratorService.nameCharsMono())
                .expectNext(List.of("c", "a", "r", "l"))
                .verifyComplete();
    }

    @Test
    void nameMonoConcatWith() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameMonoConcatWith(Mono.just("bob")))
                .expectNext("carl", "bob")
                .verifyComplete();
    }
}
