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
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .expectNextCount(2)
                .verifyComplete();
    }
}