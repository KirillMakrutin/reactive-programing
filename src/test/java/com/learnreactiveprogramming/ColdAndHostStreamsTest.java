package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

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
}
