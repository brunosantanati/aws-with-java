package com.dynamodb.dto;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@DynamoDBTable(tableName = "music")
public class Song {
	
	private String artistName;
	private String songName;
	private String albumTitle;
	
	public Song() {}
	
	public Song(String artistName, String songName, String albumTitle) {
		this.artistName = artistName;
		this.songName = songName;
		this.albumTitle = albumTitle;
	}

	@DynamoDBAttribute(attributeName = "ArtistName")
	public String getArtistName() {
		return artistName;
	}
	
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	
	@DynamoDBAttribute(attributeName = "SongName")
	public String getSongName() {
		return songName;
	}
	
	public void setSongName(String songName) {
		this.songName = songName;
	}
	
	@DynamoDBAttribute(attributeName = "AlbumTitle")
	public String getAlbumTitle() {
		return albumTitle;
	}
	
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public static Song attributeMapToSong(Map<String, AttributeValue> attributeMap) {
		String artistName = attributeMap.get("ArtistName").getS();
		String songName = attributeMap.get("SongName").getS();
		String albumTitle = attributeMap.get("AlbumTitle").getS();
		return new Song(artistName, songName, albumTitle);
	}

	@Override
	public String toString() {
		return "Song [artistName=" + artistName + ", songName=" + songName + ", albumTitle=" + albumTitle + "]";
	}

}
