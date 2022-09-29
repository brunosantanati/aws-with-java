package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonInclude
import me.brunosantana.converter.CustomBooleanAttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy
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
    @get:DynamoDbAttribute("IsWellKnown")
    @get:DynamoDbConvertedBy(CustomBooleanAttributeConverter::class)
    var isWellKnown: Boolean,
    @get:DynamoDbAttribute("IsAnInternationalSinger")
    var internationalSinger: Boolean? = null,
    @get:DynamoDbAttribute("IsAlsoActor")
    var actor: Int = 0,
    @get:DynamoDbIgnore
    val songs: MutableList<Song> = mutableListOf()
): DynamoBaseModel(
    pkType = "artist",
    pkId = name.lowercase().replace(" ", "_"),
    sk = "artist#${name.lowercase().replace(" ", "_")}",
    gsi1pk = "type#artist",
    gsi1sk = "type#artist",
) {
    constructor(
        name: String,
        nationality: String,
        isAwardWinner: Boolean,
        isWellKnown: Boolean,
        internationalSinger: Boolean?,
        actor: Int
    ) :
            this(
                name = name,
                nationality = nationality,
                isAwardWinner = isAwardWinner,
                isWellKnown = isWellKnown,
                internationalSinger = internationalSinger,
                actor = actor,
                songs = mutableListOf()
            )

    @Deprecated(message = "Intended to be used only by AWS SDK")
    constructor() :
            this(
                name = "",
                nationality = "",
                isAwardWinner = false,
                isWellKnown = true,
                internationalSinger = null,
                actor = 0,
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
            val isWellKnownNumber = attributeMap["IsWellKnown"]?.n()
            val isWellKnown = if(isWellKnownNumber != null) isWellKnownNumber.toInt() != 0 else false
            val internationalSinger = attributeMap["IsAnInternationalSinger"]?.bool()
            val actor = attributeMap["IsAlsoActor"]?.n()?.toInt() ?: 0
            val versionTimestamp = attributeMap["VersionTimestamp"]?.s()
            val artist = Artist(
                name = name,
                nationality = nationality,
                isAwardWinner = isAwardWinner,
                isWellKnown = isWellKnown,
                internationalSinger = internationalSinger,
                actor = actor)
            artist.versionTimestamp = versionTimestamp
            return artist
        }
    }
}
