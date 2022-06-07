package com.dynamodb.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.dynamodb.dto.Movie;
import com.dynamodb.repositories.MovieDynamoRepository;
import com.dynamodb.repositories.MovieRepository;

@Service
public class MovieSearchService {
	
	private MovieRepository movieRepository;
	private MovieDynamoRepository movieDynamoRepository; 
	
	public MovieSearchService(MovieRepository movieRepository, MovieDynamoRepository movieDynamoRepository) {
        this.movieRepository = movieRepository;
        this.movieDynamoRepository = movieDynamoRepository;
    }

	public List<Movie> findAllMovies() {
        return StreamSupport.stream(movieRepository.findAll().spliterator(), true).collect(Collectors.toList());
    }
	
	public void findMovieById(String filmId) {
		movieDynamoRepository.findByKey(filmId);
	}

}
