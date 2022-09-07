package com.dynamodb.service;

import com.dynamodb.dto.Artist;
import com.dynamodb.repositories.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicService {
	
	@Autowired
	MusicRepository musicRepository;
	
	public Artist findArtistByName(String name) {
		return musicRepository.findArtistByName(name);
	}

}
