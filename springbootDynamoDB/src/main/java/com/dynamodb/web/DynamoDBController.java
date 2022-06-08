package com.dynamodb.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dynamodb.dto.Movie;
import com.dynamodb.service.MovieSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("movies")
public class DynamoDBController {

	private MovieSearchService movieSearchService;

	DynamoDBController(MovieSearchService movieSearchService) {
		this.movieSearchService = movieSearchService;
	}

	@RequestMapping(value = "/retrieveAllMovies", method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> retrieveAllMovies() {
		MultiValueMap<String, String> headers = new HttpHeaders();

		Iterable<Movie> movies = movieSearchService.findAllMovies();

		if (movies != null) {
			System.out.println("Movies are not Null");
		}

		List<Movie> moviesList = StreamSupport.stream(movies.spliterator(), false).collect(Collectors.toList());

		return new ResponseEntity<List<Movie>>(moviesList, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{filmId}", method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> retrieveMovieById(@PathVariable String filmId) throws JsonMappingException, JsonProcessingException {
		List<Movie> movies = movieSearchService.findMovieById(filmId);
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/filmId/{filmId}", method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> retrieveMovieByKey(@PathVariable String filmId) throws JsonMappingException, JsonProcessingException {
		List<Movie> movies = movieSearchService.findMovieByKey(filmId);
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
	}

}
