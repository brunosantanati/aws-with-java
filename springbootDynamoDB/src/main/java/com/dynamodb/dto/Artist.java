package com.dynamodb.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoDBTable(tableName = "music")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Artist {
	
	private String name;
	private String nationality;
	private List<Song> songs = new ArrayList<>();
	
	public Artist() {
	}

	public Artist(String name, String nationality) {
		this.name = name;
		this.nationality = nationality;
	}

	@DynamoDBAttribute(attributeName = "ArtistName")
	@JsonProperty("ArtistName")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@DynamoDBAttribute(attributeName = "Nationality")
	@JsonProperty("Nationality")
	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	@DynamoDBIgnore
	public List<Song> getSongs() {
		return songs;
	}
	
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public static Artist attributeMapToArtist(Map<String, AttributeValue> attributeMap) {
		String name = attributeMap.get("ArtistName").getS();
		String nationality = attributeMap.get("Nationality").getS();
		return new Artist(name, nationality);
	}

	@Override
	public String toString() {
		return "Artist [name=" + name + ", nationality=" + nationality + ", songs=" + songs + "]";
	}

}
