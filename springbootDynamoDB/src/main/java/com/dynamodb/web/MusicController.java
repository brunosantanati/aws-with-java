package com.dynamodb.web;

import java.util.List;

import com.dynamodb.dto.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dynamodb.dto.Artist;
import com.dynamodb.service.MusicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/*
In the case of the music table, I've created a table with a partition key (pk), a sort key (sk),
a global secondary index (gsi1) with a partition key(gsi1pk) and a sort key(gsi1sk)
and a global secondary index(gsi3) with sk as partition key.
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
	
	@RequestMapping(value = "/artists", method = RequestMethod.GET)
	public ResponseEntity<String> getArtistsByName() {
		musicService.findArtistsByName();
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ResponseEntity<List<Artist>> queryIndex() throws JsonMappingException, JsonProcessingException {
		List<Artist> artists = musicService.queryIndex();
		return new ResponseEntity<List<Artist>>(artists, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/index2", method = RequestMethod.GET)
	public ResponseEntity<List<Artist>> queryIndex2() {
		List<Artist> artists = musicService.queryIndex2();
		return new ResponseEntity<List<Artist>>(artists, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/index3", method = RequestMethod.GET)
	public ResponseEntity<List<Artist>> queryIndex3() {
		List<Artist> artists = musicService.queryIndex3();
		return new ResponseEntity<List<Artist>>(artists, HttpStatus.OK);
	}

	@RequestMapping(value = "/gsi3", method = RequestMethod.GET)
	public ResponseEntity<Song> queryIndexToGetSong() {
		Song song = musicService.queryIndexToGetSong();
		return new ResponseEntity<>(song, HttpStatus.OK);
	}

	@RequestMapping(value = "/gsi3-2", method = RequestMethod.GET)
	public ResponseEntity<Song> queryIndexToGetASong() {
		Song song = musicService.queryIndexToGetASong();
		return new ResponseEntity<>(song, HttpStatus.OK);
	}

}
