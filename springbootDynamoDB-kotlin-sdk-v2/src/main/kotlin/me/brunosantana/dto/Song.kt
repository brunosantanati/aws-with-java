package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

private const val TYPE = "SONG"

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
//@JsonIgnoreProperties(value = ["pk", "sk", "gsi1pk", "gsi1sk"])
data class Song (
    @get:DynamoDbPartitionKey
    var pk: String,
    @get:DynamoDbSortKey
    var sk: String,
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["gsi1"])
    var gsi1pk: String,
    @get:DynamoDbSecondarySortKey(indexNames = ["gsi1"])
    var gsi1sk: String,
    @get:DynamoDbAttribute("ArtistName")
    var artistName: String,
    @get:DynamoDbAttribute("SongName")
    var songName: String,
    @get:DynamoDbAttribute("AlbumTitle")
    var albumTitle: String
) {

    constructor(artistName: String,
                songName: String,
                albumTitle: String) :
            this(
                pk = "",
                sk = "",
                gsi1pk = "",
                gsi1sk = "",
                artistName = artistName,
                songName = songName,
                albumTitle = albumTitle
            )

    @Deprecated(message = "Intended to be used only by AWS SDK")
    constructor() :
            this(
                pk = "",
                sk = "",
                gsi1pk = "",
                gsi1sk = "",
                artistName = "",
                songName = "",
                albumTitle = ""
            )

    @DynamoDbAttribute("Type")
    fun getType(): String {
        return TYPE
    }

    fun setType(type: String) {
        // Do nothing, this setter is just to make the AWS SDK 2.x happy
    }

    companion object {
        fun attributeMapToSong(attributeMap: Map<String, AttributeValue>): Song {
            val artistName = attributeMap["ArtistName"]!!.s()
            val songName = attributeMap["SongName"]!!.s()
            val albumTitle = attributeMap["AlbumTitle"]!!.s()
            return Song(artistName, songName, albumTitle)
        }
    }
}
