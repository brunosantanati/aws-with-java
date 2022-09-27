package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonInclude
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

private const val TYPE = "ARTIST"

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Artist(
    @get:DynamoDbAttribute("ArtistName")
    var name: String,
    @get:DynamoDbAttribute("Nationality")
    var nationality: String,
    @get:DynamoDbAttribute("IsAwardWinner")
    var isAwardWinner: Boolean,
    @get:DynamoDbAttribute("IsAnInternationalSinger")
    var isAnInternationalSinger: Boolean? = null, //It seems nullable Boolean does not work
    @get:DynamoDbIgnore
    val songs: MutableList<Song> = mutableListOf()
): DynamoBaseModel(
    pkType = "artist",
    pkId = name.lowercase().replace(" ", "_"),
    sk = "artist#${name.lowercase().replace(" ", "_")}",
    gsi1pk = "type#artist",
    gsi1sk = "type#artist",
) {
    constructor(name: String, nationality: String, isAwardWinner: Boolean, isAnInternationalSinger: Boolean?) :
            this(
                name = name,
                nationality = nationality,
                isAwardWinner = isAwardWinner,
                isAnInternationalSinger = isAnInternationalSinger,
                songs = mutableListOf()
            )

    @Deprecated(message = "Intended to be used only by AWS SDK")
    constructor() :
            this(
                name = "",
                nationality = "",
                isAwardWinner = false,
                isAnInternationalSinger = null,
                songs = mutableListOf()
            )

    @DynamoDbAttribute("Type")
    fun getType(): String {
        return TYPE
    }

    fun setType(type: String) {
        // Do nothing, this setter is just to make the AWS SDK 2.x happy
    }

    fun addAllSongs(songs: MutableList<Song>){
        this.songs.addAll(songs)
    }

    companion object {
        fun attributeMapToArtist(attributeMap: Map<String, AttributeValue>): Artist {
            val name = attributeMap["ArtistName"]!!.s()
            val nationality = attributeMap["Nationality"]!!.s()
            val isAwardWinner = attributeMap["IsAwardWinner"]?.bool() ?: false
            val isAnInternationalSinger = attributeMap["IsAnInternationalSinger"]?.bool()
            val versionTimestamp = attributeMap["VersionTimestamp"]?.s()
            val artist = Artist(
                name = name,
                nationality = nationality,
                isAwardWinner = isAwardWinner,
                isAnInternationalSinger = isAnInternationalSinger)
            artist.versionTimestamp = versionTimestamp
            return artist
        }
    }
}
