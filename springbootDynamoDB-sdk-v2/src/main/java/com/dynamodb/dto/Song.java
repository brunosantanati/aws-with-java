package com.dynamodb.dto;

import java.util.Map;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@DynamoDbBean
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

	public String getArtistName() {
		return artistName;
	}
	
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getSongName() {
		return songName;
	}
	
	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}
	
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public static Song attributeMapToSong(Map<String, AttributeValue> attributeMap) {
		String artistName = attributeMap.get("ArtistName").s();
		String songName = attributeMap.get("SongName").s();
		String albumTitle = attributeMap.get("AlbumTitle").s();
		return new Song(artistName, songName, albumTitle);
	}

	@Override
	public String toString() {
		return "Song [artistName=" + artistName + ", songName=" + songName + ", albumTitle=" + albumTitle + "]";
	}

}
