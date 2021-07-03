package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
                .filter(name -> name.length() > filterByLength);
    }


    public Flux<String> namesFluxImmutability() {
        var flux = Flux.just("alex", "ben", "chloe");
        flux.map(String::toUpperCase);
        return flux;
    }

    public Mono<String> nameMono() {
        return Mono.just("carl").log();
    }
}
