package me.brunosantana.dto

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@DynamoDbBean
data class Song (
    var artistName: String,
    var songName: String,
    var albumTitle: String
) {
    companion object {
        fun attributeMapToSong(attributeMap: Map<String, AttributeValue>): Song {
            val artistName = attributeMap["ArtistName"]!!.s()
            val songName = attributeMap["SongName"]!!.s()
            val albumTitle = attributeMap["AlbumTitle"]!!.s()
            return Song(artistName, songName, albumTitle)
        }
    }
}
