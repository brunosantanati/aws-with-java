package com.dynamodb.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@DynamoDBTable(tableName = "music")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(value = {"pk", "sk", "gsi1pk", "gsi1sk"})
public class Artist {
	
	private String pk;
	private String sk;
	private String gsi1pk;
	private String gsi1sk;
	private String name;
	private String nationality;
	private List<Song> songs = new ArrayList<>();
	
	public Artist() {
	}

	public Artist(String name, String nationality) {
		this.name = name;
		this.nationality = nationality;
	}

	@DynamoDBHashKey(attributeName = "pk")
	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
	
	@DynamoDBRangeKey(attributeName = "sk")
	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	@DynamoDBIndexHashKey(attributeName = "gsi1pk", globalSecondaryIndexName="gsi1")
	public String getGsi1pk() {
		return gsi1pk;
	}

	public void setGsi1pk(String gsi1pk) {
		this.gsi1pk = gsi1pk;
	}
	
	@DynamoDBIndexRangeKey(attributeName = "gsi1sk", globalSecondaryIndexName="gsi1")
	public String getGsi1sk() {
		return gsi1sk;
	}

	public void setGsi1sk(String gsi1sk) {
		this.gsi1sk = gsi1sk;
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
		String name = attributeMap.get("ArtistName").s();
		String nationality = attributeMap.get("Nationality").s();
		return new Artist(name, nationality);
	}

	@Override
	public String toString() {
		return "Artist [name=" + name + ", nationality=" + nationality + ", songs=" + songs + "]";
	}

}
