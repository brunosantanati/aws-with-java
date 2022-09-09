package com.dynamodb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

	@DynamoDbPartitionKey
	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
	
	@DynamoDbSortKey
	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	@DynamoDbSecondaryPartitionKey(indexNames = {"gsi1"})
	public String getGsi1pk() {
		return gsi1pk;
	}

	public void setGsi1pk(String gsi1pk) {
		this.gsi1pk = gsi1pk;
	}
	
	@DynamoDbSecondarySortKey(indexNames = {"gsi1"})
	public String getGsi1sk() {
		return gsi1sk;
	}

	public void setGsi1sk(String gsi1sk) {
		this.gsi1sk = gsi1sk;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	@DynamoDbIgnore
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
