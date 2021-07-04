package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {
    public static void main(String[] args) {
        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
        service.namesFlux().subscribe(System.out::println);
        service.nameMono().subscribe(System.out::println);
//        service.nameMono().doOnNext(System.out::println).block();
    }

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log()
                .map(String::toUpperCase)
                .log();
    }

    public Flux<String> namesFluxFilter(int filterByLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > filterByLength)
                .defaultIfEmpty("empty")
                .log();
    }

    public Flux<String> namesFluxFilterSwitchIfEmpty(int filterByLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(name -> name.length() > filterByLength)
                .switchIfEmpty(Flux.just("switch", "if", "empty"))
                .log();
    }


    public Flux<String> namesFluxImmutability() {
        var flux = Flux.just("alex", "ben", "chloe");
        flux.map(String::toUpperCase);
        return flux;
    }

    public Flux<String> namesFluxFlatMap(String name) {
        return Flux.just(name)
                .flatMap(next -> Flux.fromArray(next.split("")));
    }

    public Mono<String> reduceFluxToMono(Flux<String> nameChars) {
        return nameChars
                .reduce("", (result, current) -> result + current.toUpperCase());
    }

    public Flux<String> namesFluxFlatMapAsync(String... names) {
        Random random = new Random();
        return Flux.fromArray(names)
                .flatMap(name ->
                        Flux.fromArray(name.split(""))
                                .delayElements(Duration.ofMillis(random.nextInt(1_000))))
//                                .delayElements(Duration.ofMillis(100)))
                .log();
    }

    public Flux<String> namesFluxFlatMapSync(String... names) {
        Random random = new Random();
        return Flux.fromArray(names)
                .concatMap(name ->
                        Flux.fromArray(name.split(""))
                                .delayElements(Duration.ofMillis(random.nextInt(1_000))))
//                                .delayElements(Duration.ofMillis(100)))
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.just("carl").log();
    }

    public Mono<List<String>> nameCharsMono() {
        return nameMono()
                .flatMap(name -> Mono.just(List.of(name.split(""))));
    }

    public Flux<String> monoFlatMapMany() {
        return nameMono()
                .flatMapMany(this::split)
                .log();
    }

    private Flux<String> split(String name) {
        return Flux.fromArray(name.split(""));
    }

    public Flux<String> nameMonoConcatWith(Mono<String> anotherMono) {
        return nameMono()
                .concatWith(anotherMono);
    }

    public Mono<String> monoDefaultIfEmpty(int length) {
        return Mono.just("alex")
                .filter(name -> name.length() > length)
                .defaultIfEmpty("defaultIfEmpty");
    }

    public Mono<String> monoSwitchIfEmpty(int length) {
        return Mono.just("alex")
                .filter(name -> name.length() > length)
                .switchIfEmpty(Mono.just("switchIfEmpty"));
    }

    public Flux<String> exploreConcat() {
        var abc = Flux.just("A", "B", "C");
        var def = Flux.just("D", "E", "F");

        return Flux.concat(abc, def);
    }

    public Flux<String> exploreConcatWith() {
        return Flux.just("A", "B", "C").concatWith(Flux.just("D", "E", "F"));
    }

    public Flux<String> exploreMonoConcatWith() {
        return Mono.just("A").concatWith(Mono.just("B"));
    }

    public Flux<String> exploreMerge() {
        var _1stFlux = Flux.just("A", "B", "C", "D", "E")
                .delayElements(Duration.ofMillis(100));
        var _2ndFlux = Flux.just("X", "Y", "Z")
                .delayElements(Duration.ofMillis(177));

        return Flux.merge(_1stFlux, _2ndFlux).log();
    }

    public Flux<String> exploreMergeWith() {
        var _1stFlux = Flux.just("A", "B", "C", "D", "E")
                .delayElements(Duration.ofMillis(100));
        var _2ndFlux = Flux.just("X", "Y", "Z")
                .delayElements(Duration.ofMillis(177));

        return _1stFlux.mergeWith(_2ndFlux).log();
    }

    public Flux<String> exploreMergeSequential() {
        var _1stFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofMillis(500));
        var _2ndFlux = Flux.just("X", "Y", "Z")
                .delayElements(Duration.ofMillis(1000));

        return Flux.mergeSequential(_1stFlux, _2ndFlux).log();
    }

    public Flux<Tuple2<String, String>> exploreZip() {
        var _1stFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofMillis(500));
        var _2ndFlux = Flux.just("X", "Y", "Z")
                .delayElements(Duration.ofMillis(1000));

        return Flux.zip(_1stFlux, _2ndFlux).log();
    }

    public Flux<String> exploreZipMap() {
        var _1stFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofMillis(500));
        var _2ndFlux = Flux.just("X", "Y", "Z")
                .delayElements(Duration.ofMillis(1000));
        var _3ndFlux = Flux.just("1", "2", "3", "4")
                .delayElements(Duration.ofMillis(800));

        return Flux.zip(_1stFlux, _2ndFlux, _3ndFlux).log().map(tuple3 -> tuple3.getT1() + tuple3.getT2() + tuple3.getT3()).log();
    }

    public Flux<String> exploreZipWith() {
        var _1stFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofMillis(500));
        var _2ndFlux = Flux.just("X", "Y", "Z")
                .delayElements(Duration.ofMillis(1000));
        var _3ndFlux = Flux.just("1", "2", "3", "4")
                .delayElements(Duration.ofMillis(800));

        return _1stFlux.zipWith(_3ndFlux, (one, other) -> one + other).log().zipWith(_2ndFlux, (one, other) -> one + other).log();
    }

    public Flux<String> exploreZipCombine() {
        var _1stFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofMillis(500));
        var _2ndFlux = Flux.just("X", "Y", "Z")
                .delayElements(Duration.ofMillis(200));

        return Flux.zip(_1stFlux, _2ndFlux, (first, second) -> first + second).log();
    }

    public Flux<String> exploreRetryWhen() {
        LocalTime localTime = LocalTime.now();

        return Flux.just("A", "B", "C")
                .log()
                .map(next -> {
                    if (localTime.plusSeconds(3).isBefore(LocalTime.now())) {
                        return next;
                    }

                    throw new RuntimeException("Exception occurred");
                })
                .retryWhen(Retry.backoff(4, Duration.ofSeconds(1)));
    }

}
