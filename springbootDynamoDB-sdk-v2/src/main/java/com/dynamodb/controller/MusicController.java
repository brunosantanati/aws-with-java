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

Using credentials
https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html

DynamoDB examples using SDK for Java 2.x
https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/java_dynamodb_code_examples.html

Java Annotations for DynamoDB
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.Annotations.html

How to migrate from DynamoDb Java SDK v1 to v2
https://serhatcan.medium.com/how-to-migrate-from-dynamodb-java-sdk-v1-to-v2-2e3660729e05

DynamoDBAttribute with DynamoDbEnhancedAsyncClient not working
https://stackoverflow.com/questions/69966894/dynamodbattribute-with-dynamodbenhancedasyncclient-not-working

DynamoDB Enhanced
https://github.com/aws/aws-sdk-java-v2/blob/master/services-custom/dynamodb-enhanced/README.md
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
