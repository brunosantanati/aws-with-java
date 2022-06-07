package com.dynamodb.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamodb.dto.Movie;
import com.dynamodb.repositories.MovieRepository;

@Service
public class MovieSearchService {
	
	@Autowired
	private MovieRepository movieRepository;

	public List<Movie> findAllMovies() {
        return StreamSupport.stream(movieRepository.findAll().spliterator(), true).collect(Collectors.toList());
    }

}
