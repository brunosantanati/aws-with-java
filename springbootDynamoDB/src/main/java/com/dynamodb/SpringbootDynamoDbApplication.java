package com.dynamodb;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDynamoDBRepositories
//@SpringBootApplication(scanBasePackages= {"com.dynamodb"})
//@EnableDynamoDBRepositories(basePackages = "com.dynamodb.repositories")
//@EntityScan("com.dynamodb.dto")
public class SpringbootDynamoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDynamoDbApplication.class, args);
	}

}
