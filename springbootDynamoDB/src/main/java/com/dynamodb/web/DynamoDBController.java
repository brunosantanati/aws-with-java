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

/*
 * Some useful links:
 * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.html
 * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/QueryingJavaDocumentAPI.html
 * https://stackoverflow.com/questions/67429928/dynamodb-itemcollectionqueryoutcome-to-java-object
 * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.html
 * 
 * DynamoDB Partition Key vs Sort Key – What’s the Difference?
 * https://beabetterdev.com/2022/02/07/dynamodb-partition-key-vs-sort-key/

 * Core Components of Amazon DynamoDB
 * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html

 * What is the difference between partition key and sort key in amazon dynamodb?
 * https://stackoverflow.com/questions/56166332/what-is-the-difference-between-partition-key-and-sort-key-in-amazon-dynamodb
 * 
 * Batch operation
 * https://stackoverflow.com/questions/42893125/query-multiple-partition-keys-at-same-time-dynamodb-node
 * https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_BatchGetItem.html
 * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/batch-operation-document-api-java.html
 * https://www.tutorialspoint.com/dynamodb/dynamodb_batch_retrieve.htm
 * 
 * Query an index
 * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GSIJavaDocumentAPI.Example.html
 * https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GSIJavaDocumentAPI.html#GSIJavaDocumentAPI.QueryAnIndex
 * 
 * Spring Cache
 * https://www.baeldung.com/spring-cache-tutorial
 * https://www.baeldung.com/spring-boot-evict-cache
 * https://www.baeldung.com/spring-data-testing-cacheable
 * https://stackoverflow.com/questions/57083251/using-cacheable-spring-annotation-and-manually-add-to-infinispan-cache
 * 
 * Someone trying to do the same thing
 * https://stackoverflow.com/questions/54718564/can-we-add-spring-boot-cacheable-method-inside-scheduled-cache-evict-method-for
 * 
 * The @Scheduled Annotation in Spring
 * https://www.baeldung.com/spring-scheduled-tasks
 * 
 */

//In the case of the movie table, I've created a table with a partition key (FilmId), no sort key and no indexes.

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
