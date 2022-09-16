package me.brunosantana.repositories

import me.brunosantana.DateUtils
import me.brunosantana.dto.Artist
import me.brunosantana.dto.DynamoBaseModel
import me.brunosantana.dto.Song
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse


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

    fun saveModel(model: DynamoBaseModel){

        val pk = "${model.pkType}#${model.pkId}"
        val existingModel = findByPkAndSk(pk, model.sk)
        val existingVersion = existingModel?.versionTimestamp
        val incomingVersion = model.versionTimestamp!!

        try {
            when(model){
                is Artist -> {
                    save(model, incomingVersion, existingVersion, true)
                }
                is Song -> {
                    save(model, incomingVersion, existingVersion, true)
                }
            }
        }catch (e: DynamoDbException){
            e.printStackTrace()
        }
    }

    fun findByPkAndSk(pk: String, sk: String): DynamoBaseModel? {
        val tableName = "music"

        val pkAttribute = AttributeValue.builder().s(pk).build()
        val skAttribute = AttributeValue.builder().s(sk).build()

        val queryReq = QueryRequest.builder()
            .tableName(tableName)
            .consistentRead(false)
            .keyConditionExpression("pk = :pk and sk = :sk")
            .expressionAttributeValues(mapOf(":pk" to pkAttribute, ":sk" to skAttribute))
            .build()

        try {
            val queryResponse: QueryResponse = client.query(queryReq)
            queryResponse.items().firstOrNull {
                return when(it["Type"]!!.s()) {
                    "ARTIST" -> Artist.attributeMapToArtist(it)
                    "SONG" -> Song.attributeMapToSong(it)
                    else -> throw Exception("Not found")
                }
            }
        } catch (e: DynamoDbException) {
            System.err.println(e.message)
        }
        return null
    }

    private final inline fun <reified T> save(model: T, incomingVersion: String, existingVersion: String?, versioningCheck: Boolean){
        println("incoming: $incomingVersion existing: $existingVersion")

        val musicTable: DynamoDbTable<T> =
            enhancedClient.table("music", TableSchema.fromBean(T::class.java))

        if(versioningCheck){
            if(existingVersion == null){
                println("no existing version")
                musicTable.putItem(model)
            }else{

                val incomingDate = DateUtils.convertStringToZonedDateTime(incomingVersion)
                val existingDate = DateUtils.convertStringToZonedDateTime(existingVersion)

                if(DateUtils.isIncomingDateNewer(incomingDate, existingDate)){
                    println("override")
                    musicTable.putItem(model) //check how to override properly
                }else{
                    println("Skip. $incomingVersion is older than $existingVersion")
                }

            }
        }else{
            println("check disabled")
            musicTable.putItem(model)
        }
    }

    @Deprecated(message = "Use saveModel instead", replaceWith = ReplaceWith("saveModel"))
    fun saveArtist(artist: Artist){
        try {
            val artistTable: DynamoDbTable<Artist> =
                enhancedClient.table("music", TableSchema.fromBean(Artist::class.java))
            artistTable.putItem(artist)
        }catch (e: DynamoDbException){
            e.printStackTrace()
        }
    }

    @Deprecated(message = "Use saveModel instead", replaceWith = ReplaceWith("saveModel"))
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
