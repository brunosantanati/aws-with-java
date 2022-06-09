package com.dynamodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamodb.dto.Artist;
import com.dynamodb.repositories.MusicRepository;

@Service
public class MusicService {
	
	@Autowired
	MusicRepository musicRepository;
	
	public Artist findArtistByName(String name) {
		return musicRepository.findArtistByName(name);
	}

}
