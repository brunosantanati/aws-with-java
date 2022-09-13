package me.brunosantana.repositories

import me.brunosantana.dto.Artist
import me.brunosantana.dto.Song
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException
import software.amazon.awssdk.services.dynamodb.model.QueryRequest


@Repository
class MusicRepository(
    val client: DynamoDbClient,
    val enhancedClient: DynamoDbEnhancedClient
) {
    fun findArtistByName(name: String): Artist? {
        val partitionKeyName = "pk"
        val partitionKeyValue = "artist#$name"
        val tableName = "music"

        // Set up mapping of the partition name with the value.
        val attrValues = HashMap<String, AttributeValue>()
        attrValues[":r_pk"] = AttributeValue.builder()
            .s(partitionKeyValue)
            .build()
        val queryReq = QueryRequest.builder()
            .tableName(tableName)
            .keyConditionExpression("$partitionKeyName = :r_pk")
            .expressionAttributeValues(attrValues)
            .build()
        return try {
            val response = client.query(queryReq)
            val items = response.items()
            buildArtistFromQueryResponse(items)
        } catch (e: DynamoDbException) {
            System.err.println(e.message)
            throw e
        }
    }

    private fun buildArtistFromQueryResponse(items: List<Map<String, AttributeValue>>): Artist? {
        val artists = mutableListOf<Artist?>()
        val songs = mutableListOf<Song>()
        items.forEach { item: Map<String, AttributeValue> ->
            when (item["Type"]!!
                .s()) {
                "ARTIST" -> {
                    val artist: Artist = Artist.attributeMapToArtist(item)
                    if (artist != null) {
                        artists.add(artist)
                    }
                }
                "SONG" -> {
                    val song: Song = Song.attributeMapToSong(item)
                    if (song != null) {
                        songs.add(song)
                    }
                }
            }
        }
        artists[0]!!.addAllSongs(songs)
        return artists[0]
    }

    fun saveArtist(artist: Artist){
        try {
            val artistTable: DynamoDbTable<Artist> =
                enhancedClient.table("music", TableSchema.fromBean(Artist::class.java))
            artistTable.putItem(artist)
        }catch (e: DynamoDbException){
            e.printStackTrace()
        }
    }

    fun saveSong(song: Song){
        try {
            val songTable: DynamoDbTable<Song> =
                enhancedClient.table("music", TableSchema.fromBean(Song::class.java))
            songTable.putItem(song)
        }catch (e: DynamoDbException){
            e.printStackTrace()
        }
    }
}
