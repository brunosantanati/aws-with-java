package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

private const val TYPE = "ARTIST"

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(value = ["pk", "sk", "gsi1pk", "gsi1sk"])
data class Artist(
    @get:DynamoDbIgnore
    val pkType: String,
    @get:DynamoDbIgnore
    val pkId: String,
    @get:DynamoDbSortKey
    var sk: String,
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["gsi1"])
    var gsi1pk: String,
    @get:DynamoDbSecondarySortKey(indexNames = ["gsi1"])
    var gsi1sk: String,
    @get:DynamoDbAttribute("ArtistName")
    var name: String,
    @get:DynamoDbAttribute("Nationality")
    var nationality: String,
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

    @Deprecated(message = "Intended to be used only by AWS SDK")
    constructor() :
            this(
                pkType = "",
                pkId = "",
                sk = "",
                gsi1pk = "",
                gsi1sk = "",
                name = "",
                nationality = "",
                songs = mutableListOf()
            )

    @DynamoDbPartitionKey
    fun getPk(): String {
        return "${pkType}#${pkId}"
    }

    fun setPk(pk: String) {
        // Do nothing, this is a derived attribute
    }

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
            return Artist(name = name, nationality = nationality)
        }
    }
}
