package me.brunosantana.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@DynamoDbBean
class Song {
    var artistName: String? = null
    var songName: String? = null
    var albumTitle: String? = null

    constructor() {}
    constructor(artistName: String?, songName: String?, albumTitle: String?) {
        this.artistName = artistName
        this.songName = songName
        this.albumTitle = albumTitle
    }

    override fun toString(): String {
        return "Song [artistName=$artistName, songName=$songName, albumTitle=$albumTitle]"
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
