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

How to create a generic method to save any type in DynamoDB using AWS SDK 2.x?
https://stackoverflow.com/questions/73736424/how-to-create-a-generic-method-to-save-any-type-in-dynamodb-using-aws-sdk-2-x

What is this date format? 2011-08-12T20:17:46.384Z
Answer: ISO 8601
https://stackoverflow.com/questions/8405087/what-is-this-date-format-2011-08-12t201746-384z

Comparing Two ZonedDateTime Instances
https://howtodoinjava.com/java/date-time/zoneddatetime-comparison/

Comparing Dates in Java
https://www.baeldung.com/java-comparing-dates

Custom converter
https://github.com/aws/aws-sdk-java-v2/blob/master/services-custom/dynamodb-enhanced/README.md#override-the-mapping-of-a-single-attribute
https://github.com/aws/aws-sdk-java-v2/issues/1912
https://stackoverflow.com/questions/64378984/dynamodb-enum-conversion-version-2
 */

@RestController
class MusicController(
    val musicService: MusicService
) {
    @RequestMapping(value = ["/artist/{artistName}"], method = [RequestMethod.GET])
    fun getArtistByName(@PathVariable artistName: String): ResponseEntity<Artist> {
        val artist: Artist = musicService.findArtistByName(artistName)
        return ResponseEntity<Artist>(artist, HttpStatus.OK)
    }

    @RequestMapping(value = ["/artist"], method = [RequestMethod.POST])
    fun saveArtist(@RequestBody artist: Artist): ResponseEntity<ApiResponse> {
        musicService.saveArtist(artist)
        return ResponseEntity<ApiResponse>(ApiResponse("Artist saved successfully"), HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/song"], method = [RequestMethod.POST])
    fun saveSong(@RequestBody song: Song): ResponseEntity<ApiResponse> {
        musicService.saveSong(song)
        return ResponseEntity<ApiResponse>(ApiResponse("Song saved successfully"), HttpStatus.CREATED)
    }
}
