package com.dynamodb.controller;

import com.dynamodb.dto.Artist;
import com.dynamodb.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//In the case of the music table, I've created a table with a partition key (pk), a sort key (sk) and a global secondary index (gsi1pk and gsi1sk).

/*
Migrating from version 1.x to 2.x of the AWS SDK for Java
https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/migration.html

What's different between the AWS SDK for Java 1.x and 2.x
https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/migration-whats-different.html
*/

@RestController
public class MusicController {
	
	@Autowired
	MusicService musicService;
	
	@RequestMapping(value = "/artist/{artistName}", method = RequestMethod.GET)
	public ResponseEntity<Artist> getArtistByName(@PathVariable String artistName) {
		Artist artist = musicService.findArtistByName(artistName);
		return new ResponseEntity<Artist>(artist, HttpStatus.OK);
	}

}
