package com.dynamodb.repositories;

import com.dynamodb.dto.Artist;
import com.dynamodb.dto.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MusicRepository {

	@Autowired
	private DynamoDbClient client;

	public Artist findArtistByName(String name) {
		
		String partitionKeyName = "pk";
		String partitionKeyValue = "artist#" + name;
		String tableName = "music";

		// Set up mapping of the partition name with the value.
		HashMap<String, AttributeValue> attrValues = new HashMap<>();
		attrValues.put(":r_pk", AttributeValue.builder()
				.s(partitionKeyValue)
				.build());

		QueryRequest queryReq = QueryRequest.builder()
				.tableName(tableName)
				.keyConditionExpression(partitionKeyName + " = :r_pk")
				.expressionAttributeValues(attrValues)
				.build();

		try {
			QueryResponse response = client.query(queryReq);
			List<Map<String, AttributeValue>> items = response.items();

			return buildArtistFromQueryResponse(items);

		} catch (DynamoDbException e) {
			System.err.println(e.getMessage());
			throw e;
		}
	}
	
	private Artist buildArtistFromQueryResponse(List<Map<String,AttributeValue>> items){
		List<Artist> artists = new ArrayList<>();
		List<Song> songs = new ArrayList<>();
		
		items.forEach(item -> {
			switch (item.get("Type").s()) {
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
