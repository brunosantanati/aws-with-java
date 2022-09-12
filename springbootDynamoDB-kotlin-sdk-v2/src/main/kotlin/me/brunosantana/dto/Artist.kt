package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonInclude
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Artist(
    @get:DynamoDbIgnore
    val pkType: String,
    @get:DynamoDbIgnore
    val pkId: String,
    @get:DynamoDbSortKey
    val sk: String,
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["gsi1"])
    val gsi1pk: String,
    @get:DynamoDbSecondarySortKey(indexNames = ["gsi1"])
    val gsi1sk: String,
    val name: String,
    val nationality: String,
    @get:DynamoDbIgnore
    val songs: MutableList<Song> = mutableListOf()
) {
    constructor(name: String, nationality: String) :
            this(
                pkType = "",
                pkId = "",
                sk = "",
                gsi1pk = "",
                gsi1sk = "",
                name = name,
                nationality = nationality,
                songs = mutableListOf()
            )

    @DynamoDbPartitionKey
    fun getPK(): String {
        return "${pkType}#${pkId}"
    }

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
