package me.brunosantana.controller

import me.brunosantana.dto.Artist
import me.brunosantana.dto.Song
import me.brunosantana.service.MusicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

/*
Introducing enhanced DynamoDB client in the AWS SDK for Java v2
https://aws.amazon.com/blogs/developer/introducing-enhanced-dynamodb-client-in-the-aws-sdk-for-java-v2/

Replace this old API with the DynamoDB V2 Enhanced Client
https://stackoverflow.com/questions/67631368/what-is-the-difference-between-dynamodbmapper-and-table-for-dynamodb-tables

It actually has the Mapper within version 2 (the guy is talking about things inside software.amazon.awssdk.enhanced.dynamodb package)
https://stackoverflow.com/questions/58438895/can-we-use-both-aws-dynamodb-version-1-x-and-2-x-in-same-project

java.lang.IllegalArgumentException: Attempt to execute an operation that requires a primary index without defining any primary key attributes in the table metadata.
https://davidagood.com/dynamodb-enhanced-client-java-missing-setters/

Annotations
https://kotlinlang.org/docs/annotations.html

DynamoDB Enhanced to support immutable Kotlin data classes #2096
https://github.com/aws/aws-sdk-java-v2/issues/2096
 */

@RestController
class MusicController {
    @Autowired
    var musicService: MusicService? = null
    @RequestMapping(value = ["/artist/{artistName}"], method = [RequestMethod.GET])
    fun getArtistByName(@PathVariable artistName: String): ResponseEntity<Artist> {
        val artist: Artist = musicService!!.findArtistByName(artistName)
        return ResponseEntity<Artist>(artist, HttpStatus.OK)
    }

    @RequestMapping(value = ["/artist/save/{artistName}"], method = [RequestMethod.GET]) //it should be POST and get data from the request body, but it's easier and quicker to test it using GET in the browser. I'm focusing on testing aws sdk 2.x.
    fun saveArtist(@PathVariable artistName: String): ResponseEntity<String> {
        val artist = Artist(
            pkType = "artist",
            pkId = artistName,
            sk = "artist#$artistName",
            gsi1pk = "type#artist",
            gsi1sk = "type#artist",
            name = artistName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            nationality = "UK",
            songs = mutableListOf()
        )
        musicService!!.saveArtist(artist)
        return ResponseEntity<String>("OK", HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/artist/save-song/{songName}"], method = [RequestMethod.GET]) //it should be POST and get data from the request body, but it's easier and quicker to test it using GET in the browser. I'm focusing on testing aws sdk 2.x.
    fun saveSong(@PathVariable songName: String): ResponseEntity<String> {
        val song = Song(
            pk = "artist#eminem",
            sk = "song#eminem#song1",
            gsi1pk = "type#song",
            gsi1sk = "type#song",
            artistName = "Eminem",
            songName = songName,
            albumTitle = "Eminem Album 1"
        )
        musicService!!.saveSong(song)
        return ResponseEntity<String>("OK", HttpStatus.CREATED)
    }
}
