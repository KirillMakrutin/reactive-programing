package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ParallelTest {
    @Test
    public void testParallel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux.range(1, 10)
                .parallel()
                .runOn(Schedulers.parallel())
//                .subscribeOn(Schedulers.parallel())
                .map(this::transform)
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
