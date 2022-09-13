package me.brunosantana.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.http.apache.ProxyConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.time.Duration

@Configuration
class DynamoDBConfig {
    @Bean
    fun dynamoDbClient(
        @Value("\${aws.access-key}") accessKey: String?,
        @Value("\${aws.secret-key}") secretKey: String?
    ): DynamoDbClient {
        val proxyConfig =
            ProxyConfiguration.builder()
        val httpClientBuilder = ApacheHttpClient.builder()
            .proxyConfiguration(proxyConfig.build())
            .maxConnections(10)
            .connectionTimeout(Duration.ofMillis(1000))
            .socketTimeout(Duration.ofMillis(1000))
        val overrideConfig = ClientOverrideConfiguration.builder()
        val awsCredentials = AwsBasicCredentials.create(
            accessKey,
            secretKey
        )
        return DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .httpClientBuilder(httpClientBuilder)
            .overrideConfiguration(overrideConfig.build())
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build()
    }

    @Bean
    fun dynamoDbEnhancedClient(
        @Autowired dynamoDbClient: DynamoDbClient
    ): DynamoDbEnhancedClient{
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    }
}
