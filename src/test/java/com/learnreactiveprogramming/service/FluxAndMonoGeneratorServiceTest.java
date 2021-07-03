package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
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
    void namesFluxFilterDefault() {
        var flux = fluxAndMonoGeneratorService
                .namesFluxFilter(5);

        StepVerifier.create(flux)
                .expectNext("empty")
                .verifyComplete();
    }

    @Test
    void namesFluxFilterSwitchIfEmpty() {
        var flux = fluxAndMonoGeneratorService
                .namesFluxFilterSwitchIfEmpty(5);

        StepVerifier.create(flux)
                .expectNext("switch", "if", "empty")
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

    @Test
    void monoFlatMapMany() {
        StepVerifier.create(fluxAndMonoGeneratorService.monoFlatMapMany())
                .expectNext("c", "a", "r", "l")
                .verifyComplete();
    }

    @Test
    void reduceFluxToMono() {
        StepVerifier.create(fluxAndMonoGeneratorService.reduceFluxToMono(Flux.just("a", "le", "x")))
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    void monoDefaultIfEmpty() {
        StepVerifier.create(fluxAndMonoGeneratorService.monoDefaultIfEmpty(5))
                .expectNext("defaultIfEmpty")
                .verifyComplete();
    }


    @Test
    void monoSwitchIfEmpty() {
        StepVerifier.create(fluxAndMonoGeneratorService.monoSwitchIfEmpty(5))
                .expectNext("switchIfEmpty")
                .verifyComplete();
    }

    @Test
    void exploreConcat() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreConcat())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWith() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreConcatWith())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreMonoConcatWith() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreMonoConcatWith())
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void exploreMerge() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreMerge())
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    void exploreMergeSequential() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreMergeSequential())
                .expectNext("A", "B", "C", "D", "E", "F", "X", "Y", "Z")
                .verifyComplete();
    }

    @Test
    void exploreMergeWith() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreMergeWith())
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    void exploreZip() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreZip())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void exploreZipMap() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreZipMap())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void exploreZipWith() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreZipWith())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void exploreZipCombine() {
        StepVerifier.create(fluxAndMonoGeneratorService.exploreZipCombine())
                .expectNext("AX", "BY", "CZ")
                .verifyComplete();
    }
}
