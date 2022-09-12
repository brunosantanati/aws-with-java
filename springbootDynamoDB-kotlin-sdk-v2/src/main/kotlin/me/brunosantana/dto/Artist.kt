package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonInclude
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@DynamoDbBean
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Artist {
    @get:DynamoDbPartitionKey
    var pk: String? = null

    @get:DynamoDbSortKey
    var sk: String? = null

    @get:DynamoDbSecondaryPartitionKey(indexNames = ["gsi1"])
    var gsi1pk: String? = null

    @get:DynamoDbSecondarySortKey(indexNames = ["gsi1"])
    var gsi1sk: String? = null
    var name: String? = null
    var nationality: String? = null
    private var songs: List<Song> = ArrayList<Song>()

    constructor() {}
    constructor(name: String?, nationality: String?) {
        this.name = name
        this.nationality = nationality
    }

    @DynamoDbIgnore
    fun getSongs(): List<Song> {
        return songs
    }

    fun setSongs(songs: List<Song>) {
        this.songs = songs
    }

    override fun toString(): String {
        return "Artist [name=$name, nationality=$nationality, songs=$songs]"
    }

    companion object {
        fun attributeMapToArtist(attributeMap: Map<String, AttributeValue>): Artist {
            val name = attributeMap["ArtistName"]!!.s()
            val nationality = attributeMap["Nationality"]!!.s()
            return Artist(name, nationality)
        }
    }
}
