package com.dynamodb.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "music")
public class Song {
	
	private String artistName;
	private String songName;
	private String albumTitle;
	
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

}
