package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static reactor.core.Exceptions.propagate;

@Slf4j
public class BackpressureTest {

    @Test
    public void testBackPressure() {

        final var rangeFlux = Flux.range(1, 100).log();

//        rangeFlux.subscribe(next -> log.info("Number is {}", next));
        rangeFlux.subscribe(new BaseSubscriber<>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                log.info("Number is {}", value);
                if (value > 50) {
                    cancel();
                } else {
                    request(2);
                }
            }

            @Override
            protected void hookOnCancel() {
                log.info("Hook on cancel");
            }
        });
    }

    @Test
    public void testBackPressure_2() {

        final var rangeFlux = Flux.range(1, 100).log().subscribeOn(Schedulers.boundedElastic());

        AtomicBoolean continueRead = new AtomicBoolean(true);
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);

        rangeFlux.subscribe(new BaseSubscriber<>() {
            @Override
            protected void hookOnNext(Integer value) {
                try {
                    blockingQueue.put(value);
                } catch (InterruptedException e) {
                    throw propagate(e);
                }
            }

            @Override
            protected void hookFinally(SignalType type) {
                stop();
            }

            private void stop() {
                continueRead.set(false);
            }
        });

        while (continueRead.get()) {
            try {
                log.info("Next number is: {}", blockingQueue.take());
                MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw propagate(e);
            }
        }
    }

    @Test
    public void testBackPressure_onBackPressureDrop() {

        final var rangeFlux = Flux.range(1, 100).log();

        rangeFlux.onBackpressureDrop(item -> log.info("Dropped item is: {}", item))
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("Next number: {}", value);
                    }
                });
    }

    @Test
    public void testBackPressure_onBackPressureBuffer() {

        final var rangeFlux = Flux.range(1, 100).log();

        rangeFlux.onBackpressureBuffer(10)
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("Next number: {}", value);
                    }
                });
    }

    @Test
    public void testBackPressure_onBackPressureError() {

        final var rangeFlux = Flux.range(1, 100).log();

        rangeFlux.onBackpressureError()
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("Next number: {}", value);
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        log.error("Error occurred:", throwable);
                    }
                });
    }
}
