package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonInclude
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

private const val TYPE = "SONG"

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Song (
    @get:DynamoDbAttribute("ArtistName")
    var artistName: String,
    @get:DynamoDbAttribute("SongName")
    var songName: String,
    @get:DynamoDbAttribute("AlbumTitle")
    var albumTitle: String
): DynamoBaseModel(
    pkType = "artist",
    pkId = artistName.lowercase().replace(" ", "_"),
    sk = "song#${artistName.lowercase().replace(" ", "_")}#${songName.lowercase().replace(" ", "_")}",
    gsi1pk = "type#song",
    gsi1sk = "type#song",
) {

    @Deprecated(message = "Intended to be used only by AWS SDK")
    constructor() :
            this(
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
