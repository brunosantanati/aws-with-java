package me.brunosantana.service

import me.brunosantana.dto.Artist
import me.brunosantana.dto.Song
import me.brunosantana.repositories.MusicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class MusicService {
	@Autowired
	var musicRepository: MusicRepository? = null
	fun findArtistByName(name: String): Artist {
		return musicRepository!!.findArtistByName(name) ?: Artist(name = "", nationality = "")
	}

	fun saveArtist(artist: Artist){
		musicRepository!!.saveArtist(artist)
	}

	fun saveSong(song: Song){
		musicRepository!!.saveSong(song)
	}
}
