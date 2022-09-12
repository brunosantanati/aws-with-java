package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonInclude
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Artist(
    @get:DynamoDbPartitionKey
    val pk: String? = null,
    @get:DynamoDbSortKey
    val sk: String? = null,
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["gsi1"])
    val gsi1pk: String? = null,
    @get:DynamoDbSecondarySortKey(indexNames = ["gsi1"])
    val gsi1sk: String? = null,
    val name: String,
    val nationality: String,
    @get:DynamoDbIgnore
    val songs: MutableList<Song> = mutableListOf()
) {
    fun addAllSongs(songs: MutableList<Song>){
        this.songs.addAll(songs)
    }

    companion object {
        fun attributeMapToArtist(attributeMap: Map<String, AttributeValue>): Artist {
            val name = attributeMap["ArtistName"]!!.s()
            val nationality = attributeMap["Nationality"]!!.s()
            return Artist(name = name, nationality = nationality)
        }
    }
}
