package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ParallelTest {
    @Test
    public void testParallel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux.range(1, 6)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::transform)
                .log()
                .doOnComplete(latch::countDown)
                .subscribe(next -> log.info("Next: {}", next));

        latch.await();

        log.info("Exit");
    }

    @Test
    public void testParallelFlatMap() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux.range(1, 6)
                .flatMap(num -> Mono.just(num).map(this::transform).subscribeOn(Schedulers.parallel()))
                .log()
                .doOnComplete(latch::countDown)
                .subscribe(next -> log.info("Next: {}", next));

        latch.await();

        log.info("Exit");
    }

    private String transform(Integer num) {
        try {
            SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "a" + num;
    }

}
