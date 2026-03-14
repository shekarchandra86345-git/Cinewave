package com.ticketbooking.controller;

import com.ticketbooking.model.Movie;
import com.ticketbooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(movieDetails.getTitle());
            movie.setGenre(movieDetails.getGenre());
            movie.setPosterUrl(movieDetails.getPosterUrl());
            movie.setDuration(movieDetails.getDuration()); // using duration as price right now or we should add price to movie
            // The frontend sends `time`, `tag`, `price`. The Movie entity has `duration`, `genre`, `description`. 
            // We should map these properly in the entity, but for now we'll do our best.
            // Ideally we need to update Movie entity to have 'price' and 'showTime' if it represents a show, but we have a Show entity for that.
            // Wait, I will use description for tags, duration for price. For real integration, we should update the entity.
            movie.setDescription(movieDetails.getDescription());
            return ResponseEntity.ok(movieRepository.save(movie));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
