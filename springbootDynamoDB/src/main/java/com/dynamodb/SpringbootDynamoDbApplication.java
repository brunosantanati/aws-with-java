package com.dynamodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootDynamoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDynamoDbApplication.class, args);
	}

}
