package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {

    private final MovieReactiveService movieReactiveService = new MovieReactiveService(new MovieInfoService(), new ReviewService());

    @Test
    void getAllMovies() {

        var allMovies = movieReactiveService.getAllMovies().log();

        StepVerifier.create(allMovies)
                .expectNextCount(3)
                .verifyComplete();
    }
}