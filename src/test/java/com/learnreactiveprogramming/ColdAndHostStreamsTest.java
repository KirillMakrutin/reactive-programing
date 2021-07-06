package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

@Slf4j
public class ColdAndHostStreamsTest {
    @Test
    public void testColdPublisher() throws InterruptedException {
        final var rangeFlux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(100));

        rangeFlux.subscribe(num -> log.info("1st subscriber: {}", num));
        rangeFlux.subscribe(num -> log.info("2nd subscriber: {}", num));

        SECONDS.sleep(2);
    }

    @Test
    void testHostPublisher() throws InterruptedException {
        final var connectableFlux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .publish();

        connectableFlux.subscribe(num -> log.info("1st subscriber: {}", num));

        SECONDS.sleep(5);

        connectableFlux.subscribe(num -> log.info("2nd subscriber: {}", num));

        SECONDS.sleep(6);

    }

    @Test
    void testHotSinksPublisher() {
        Sinks.Many<String> hotSource = Sinks.unsafe().many()
                .multicast()
                .directBestEffort();

        Flux<String> hotFlux = hotSource.asFlux().map(String::toUpperCase);

        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));

        hotSource.emitNext("blue", FAIL_FAST);
        hotSource.tryEmitNext("green").orThrow();

        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));

        hotSource.emitNext("orange", FAIL_FAST);
        hotSource.emitNext("purple", FAIL_FAST);
        hotSource.emitComplete(FAIL_FAST);
    }
}
