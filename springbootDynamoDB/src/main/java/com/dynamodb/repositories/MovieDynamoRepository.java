package com.dynamodb.repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.dynamodb.dto.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class MovieDynamoRepository {

	@Autowired
	private AmazonDynamoDB client;
	
	@Autowired
	private ObjectMapper objectMapper;

	public List<Movie> findByKey(String key) throws JsonMappingException, JsonProcessingException {
		
		List<Movie> movies = new ArrayList<>();
		
//		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//				.withRegion(Regions.US_WEST_2).build();
		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("movie");

		QuerySpec spec = new QuerySpec().withKeyConditionExpression("FilmId = :film_id")
				.withValueMap(new ValueMap().withString(":film_id", key));

		ItemCollection<QueryOutcome> items = table.query(spec);

		Iterator<Item> iterator = items.iterator();
		Item item = null;
		while (iterator.hasNext()) {
			item = iterator.next();
			System.out.println(item.toJSONPretty());
			
			Movie movie = objectMapper.readValue(item.toJSONPretty(), Movie.class);
			movies.add(movie);
		}
		
		return movies;

	}
	
	public List<Movie> findMovieByKey(String key){
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		
		Movie movieToSearch = new Movie();
		movieToSearch.setFilmId(key);
		
		DynamoDBQueryExpression<Movie> queryExpression = new DynamoDBQueryExpression<Movie>()
			    .withHashKeyValues(movieToSearch);
		
		return mapper.query(Movie.class, queryExpression);

	}

}
