package me.brunosantana.controller

import me.brunosantana.dto.ApiResponse
import me.brunosantana.dto.Artist
import me.brunosantana.dto.Song
import me.brunosantana.service.MusicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
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

    @RequestMapping(value = ["/artist"], method = [RequestMethod.POST])
    fun saveArtist(@RequestBody artist: Artist): ResponseEntity<ApiResponse> {
        musicService!!.saveArtist(artist)
        return ResponseEntity<ApiResponse>(ApiResponse("Artist saved successfully"), HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/song"], method = [RequestMethod.POST])
    fun saveSong(@RequestBody song: Song): ResponseEntity<ApiResponse> {
        musicService!!.saveSong(song)
        return ResponseEntity<ApiResponse>(ApiResponse("Song saved successfully"), HttpStatus.CREATED)
    }
}
