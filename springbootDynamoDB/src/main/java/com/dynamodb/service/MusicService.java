package com.dynamodb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamodb.dto.Artist;
import com.dynamodb.repositories.MusicRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class MusicService {
	
	@Autowired
	MusicRepository musicRepository;
	
	public Artist findArtistByName(String name) {
		return musicRepository.findArtistByName(name);
	}
	
	public void findArtistsByName() {
		musicRepository.findArtistsByName();
	}
	
	public List<Artist> queryIndex() throws JsonMappingException, JsonProcessingException {
		return musicRepository.queryIndex();
	}
	
	public List<Artist> queryIndex2() {
		return musicRepository.queryIndex2();
	}

}
