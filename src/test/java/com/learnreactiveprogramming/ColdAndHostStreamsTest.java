package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
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
    void testHotPublisher() throws InterruptedException {
        final var connectableFlux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .publish();

        connectableFlux.connect();

        SECONDS.sleep(2);

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

    @Test
    void testHotPublisherAutoConnect() throws InterruptedException {
        final var connectableFlux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .publish()
                .autoConnect(1);

        SECONDS.sleep(2);

        connectableFlux.subscribe(num -> log.info("1st subscriber: {}", num));

        SECONDS.sleep(5);

        connectableFlux.subscribe(num -> log.info("2nd subscriber: {}", num));

        SECONDS.sleep(6);

    }

    @Test
    void testHotPublisherRefCount() throws InterruptedException {
        final var connectableFlux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnSubscribe((subscription) -> log.info("Get subscribe signal"))
                .doOnCancel(() -> log.info("Get cancel signal"))
                .publish()
                .refCount(2);


        Disposable subscriber1 = connectableFlux.subscribe(num -> log.info("1st subscriber: {}", num));
        Disposable subscriber2 = connectableFlux.subscribe(num -> log.info("2nd subscriber: {}", num));

        SECONDS.sleep(3);

        subscriber1.dispose();
        subscriber2.dispose();

        SECONDS.sleep(4);

        connectableFlux.subscribe(num -> log.info("3rd subscriber: {}", num));
        connectableFlux.subscribe(num -> log.info("4th subscriber: {}", num));

        SECONDS.sleep(4);
    }
}
