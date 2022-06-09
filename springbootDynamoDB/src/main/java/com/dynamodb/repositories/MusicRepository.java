package com.dynamodb.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.dynamodb.dto.Artist;
import com.dynamodb.dto.Song;

@Repository
public class MusicRepository {

	@Autowired
	private AmazonDynamoDB client;
	
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
