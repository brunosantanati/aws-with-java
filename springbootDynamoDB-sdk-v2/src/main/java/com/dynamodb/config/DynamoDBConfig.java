package com.dynamodb.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Duration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.dynamodb.repositories")
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient(@Value("${aws.access-key}") String accessKey,
                                         @Value("${aws.secret-key}") String secretKey

    ) {

        ProxyConfiguration.Builder proxyConfig = ProxyConfiguration.builder();

        ApacheHttpClient.Builder httpClientBuilder =
                ApacheHttpClient.builder()
                        .proxyConfiguration(proxyConfig.build())
                        .maxConnections(10)
                        .connectionTimeout(Duration.ofMillis(1000))
                        .socketTimeout(Duration.ofMillis(1000));

        ClientOverrideConfiguration.Builder overrideConfig =
                ClientOverrideConfiguration.builder();

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                accessKey,
                secretKey);

        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_2)
                .httpClientBuilder(httpClientBuilder)
                .overrideConfiguration(overrideConfig.build())
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        return dynamoDbClient;
    }

}
