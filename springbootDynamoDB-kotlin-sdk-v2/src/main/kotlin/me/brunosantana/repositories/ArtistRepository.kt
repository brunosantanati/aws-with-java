package me.brunosantana.repositories

import me.brunosantana.dto.Artist
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class ArtistRepository(
    val enhancedClient: DynamoDbEnhancedClient
) {

    fun save(artist: Artist){
        val artistTable: DynamoDbTable<Artist> =
            enhancedClient.table("music", TableSchema.fromBean(Artist::class.java))
        artistTable.putItem(artist)
    }

}