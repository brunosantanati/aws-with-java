package me.brunosantana.repositories

import me.brunosantana.dto.Song
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class SongRepository(
    val enhancedClient: DynamoDbEnhancedClient
) {

    fun save(song: Song, existingVersion: String?, versioningCheck: Boolean){
        println(existingVersion)
        val songTable: DynamoDbTable<Song> =
            enhancedClient.table("music", TableSchema.fromBean(Song::class.java))
        songTable.putItem(song)
    }

}