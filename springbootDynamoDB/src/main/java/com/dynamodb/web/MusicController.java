package com.dynamodb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dynamodb.dto.Artist;
import com.dynamodb.service.MusicService;

//In the case of the music table, I've created a table with a partition key (pk), a sort key (sk) but with no indexes.

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
