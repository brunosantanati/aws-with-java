package com.dynamodb.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dynamodb.repositories.MusicRepository;

@Component
public class InitCacheAfterStartup {
	
	@Autowired
	MusicRepository musicRepository;
	
	@EventListener(ApplicationReadyEvent.class)
	public void initializeCacheAfterStartup() {
	    System.out.println("Initializing cache after startup");
	    try {
	    	musicRepository.queryIndex();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

}
