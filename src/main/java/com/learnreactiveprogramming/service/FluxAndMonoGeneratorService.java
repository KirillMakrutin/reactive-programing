package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
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

}
