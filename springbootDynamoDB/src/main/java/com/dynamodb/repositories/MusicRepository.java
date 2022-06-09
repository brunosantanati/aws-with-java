package com.dynamodb.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.dynamodb.dto.Artist;
import com.dynamodb.dto.Song;

@Repository
public class MusicRepository {

	@Autowired
	private AmazonDynamoDB client;

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
		
		List<Artist> results = buildArtistsFromQueryResult(items);
		
		return results.get(0);
	}
	
	private List<Artist> buildArtistsFromQueryResult(List<Map<String,AttributeValue>> items){
		Map<String, Artist> artists = new HashMap<>();
		Map<String, Song> songs = new HashMap<>();
		
		items.forEach(item -> {
			switch (item.get("Type").getS()) {
			case "ARTIST":
				Artist artist = Artist.attributeMapToArtist(item);
				if(artist != null) {
					artists.put(artist.getName(), artist);
				}
				break;
			case "SONG":
				Song song = Song.attributeMapToSong(item);
				if(song != null) {
					songs.put(song.getArtistName(), song);
				}
				break;
			}
		});
		
		songs.forEach((key, value) -> {
			 String artistName = key;
			 Song song = value;
			 if(artists.get(artistName) != null){
				 artists.get(artistName).getSongs().add(song);
			 }
		});
		
		return new ArrayList<Artist>(artists.values());
	}

}
