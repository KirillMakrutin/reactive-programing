package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
public class ProgrammaticallyCreateTest {
    @Test
    public void testFluxGenerate() {
        var integerFlux = Flux.generate(
                () -> 1,
                (state, sink) -> {
                    if (state > 10) {
                        sink.complete();
                    } else {
                        sink.next(state);
                        // cannot emit multiple times
                        // sink.next(state);
                    }

                    return ++state;
                }
        ).log();

        StepVerifier.create(integerFlux)
                .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .verifyComplete();
    }

    @Test
    public void testFluxCreate() {
        var namesFlux = Flux.create(fluxSink ->
                CompletableFuture.supplyAsync(this::names)
                        // can emit multiple time
                        .thenAccept(names -> names.forEach(name -> fluxSink.next(name).next(name)))
                        .thenRun(fluxSink::complete))
                .log();

        StepVerifier.create(namesFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void testMonoCreate() {
        var alexMono = Mono.create(monoSink -> CompletableFuture.runAsync(() -> monoSink.success("alex"))).log();

        StepVerifier.create(alexMono)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    public void testFluxHandle() {
        var handledFlux = Flux.fromIterable(names())
                .handle((name, sink) -> {
                    if (name.length() > 3) sink.next(name);
                    else log.info("Skip {}", name);
                }).log();

        StepVerifier.create(handledFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    private List<String> names() {
        try {
            MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw Exceptions.propagate(e);
        }

        return List.of("alex", "ben", "chloe");
    }
}
