package com.dynamodb.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.dynamodb.dto.Artist;
import com.dynamodb.dto.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
@CacheConfig(cacheNames = {"artists"})
public class MusicRepository {

	@Autowired
	private AmazonDynamoDB client;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Cacheable(cacheNames = {"artists"})
	public List<Artist> queryIndex() throws JsonMappingException, JsonProcessingException {
		return getArtists();
	}
	
	@Scheduled(fixedRateString = "30000", initialDelay = 30000)
	@CacheEvict(cacheNames = {"artists"}, allEntries = true, beforeInvocation = true)
	public void evictCache() throws JsonMappingException, JsonProcessingException {
		System.out.println("############ Evicting cache");
		//cacheArtists();
		
		System.out.println("############ Seeding cache");
		Cache cache = cacheManager.getCache("artists");
		cache.put("artists", getArtists());
	}
	
	/*
	 * @CachePut(cacheNames = {"artists"}) public List<Artist> cacheArtists() throws
	 * JsonMappingException, JsonProcessingException {
	 * System.out.println("############ Seeding cache"); return getArtists(); }
	 */
	
	private List<Artist> getArtists() throws JsonProcessingException, JsonMappingException {
		List<Artist> artists = new ArrayList<>();
		
		// AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("music");
		Index index = table.getIndex("gsi1");

		QuerySpec spec = new QuerySpec().withKeyConditionExpression("#g = :v_gsi1pk")
				.withNameMap(new NameMap().with("#g", "gsi1pk"))
				.withValueMap(new ValueMap().withString(":v_gsi1pk", "type#artist"));

		ItemCollection<QueryOutcome> items = index.query(spec);
		Iterator<Item> iter = items.iterator();
		while (iter.hasNext()) {
			String artistJson = iter.next().toJSONPretty();
			System.out.println(artistJson);
			Artist artist = objectMapper.readValue(artistJson, Artist.class);
			artists.add(artist);
		}
		
		return artists;
	}
	
	public void findArtistsByName() {
		try {

			DynamoDB dynamoDB = new DynamoDB(client);

			TableKeysAndAttributes forumTableKeysAndAttributes = new TableKeysAndAttributes("music");
			// Add a partition key
			forumTableKeysAndAttributes.addHashAndRangePrimaryKeys("pk", "sk", 
					"artist#adele", "artist#adele", 
					"artist#adele", "song#adele#rolling_in_the_deep", 
					"artist#adele", "song#adele#set_fire_to_the_rain",
					"artist#sam_smith", "artist#sam_smith",
					"artist#sam_smith", "song#sam_smith#im_not_the_only_one",
					"artist#sam_smith", "song#sam_smith#too_good_at_goodbyes");

			System.out.println("Making the request.");

			BatchGetItemOutcome outcome = dynamoDB.batchGetItem(forumTableKeysAndAttributes);

			Map<String, KeysAndAttributes> unprocessed = null;

			do {
				for (String tableName : outcome.getTableItems().keySet()) {
					System.out.println("Items in table " + tableName);
					List<Item> items = outcome.getTableItems().get(tableName);
					for (Item item : items) {
						System.out.println(item.toJSONPretty());
					}
				}

				// Check for unprocessed keys which could happen if you exceed
				// provisioned
				// throughput or reach the limit on response size.
				unprocessed = outcome.getUnprocessedKeys();

				if (unprocessed.isEmpty()) {
					System.out.println("No unprocessed keys found");
				} else {
					System.out.println("Retrieving the unprocessed keys");
					outcome = dynamoDB.batchGetItemUnprocessed(unprocessed);
				}

			} while (!unprocessed.isEmpty());

		} catch (Exception e) {
			System.err.println("Failed to retrieve items.");
			System.err.println(e.getMessage());
		}
	}

	public Artist findArtistByName(String name) {
		AttributeValue artistPk = new AttributeValue("artist#" + name);
		Map<String, AttributeValue> map = new HashMap<>();
		map.put(":r_pk", artistPk);
		
		QueryRequest queryRequest = new QueryRequest()
				.withTableName("music")
				.withConsistentRead(true) //check what is that
				.withKeyConditionExpression("pk = :r_pk")
				.withExpressionAttributeValues(map);
		
		QueryResult query = client.query(queryRequest);
		
		List<Map<String,AttributeValue>> items = query.getItems();
		
		return buildArtistFromQueryResult(items);
	}
	
	private Artist buildArtistFromQueryResult(List<Map<String,AttributeValue>> items){
		List<Artist> artists = new ArrayList<>();
		List<Song> songs = new ArrayList<>();
		
		items.forEach(item -> {
			switch (item.get("Type").getS()) {
			case "ARTIST":
				Artist artist = Artist.attributeMapToArtist(item);
				if(artist != null) {
					artists.add(artist);
				}
				break;
			case "SONG":
				Song song = Song.attributeMapToSong(item);
				if(song != null) {
					songs.add(song);
				}
				break;
			}
		});
		
		artists.get(0).setSongs(songs);
		
		return artists.get(0);
	}

}
