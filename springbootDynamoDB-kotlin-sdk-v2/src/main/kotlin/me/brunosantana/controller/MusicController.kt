package me.brunosantana.controller

import me.brunosantana.dto.Artist
import me.brunosantana.service.MusicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class MusicController {
    @Autowired
    var musicService: MusicService? = null
    @RequestMapping(value = ["/artist/{artistName}"], method = [RequestMethod.GET])
    fun getArtistByName(@PathVariable artistName: String): ResponseEntity<Artist> {
        val artist: Artist = musicService!!.findArtistByName(artistName)
        return ResponseEntity<Artist>(artist, HttpStatus.OK)
    }
}
