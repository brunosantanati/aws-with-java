package me.brunosantana.service

import me.brunosantana.dto.Artist
import me.brunosantana.dto.Song
import me.brunosantana.repositories.MusicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MusicService(
	val musicRepository: MusicRepository
) {
	fun findArtistByName(name: String): Artist {
		return musicRepository.findArtistByName(name) ?:
			Artist(
				name = "",
				nationality = "",
				isAwardWinner = false,
				internationalSinger = null,
				actor = 0
			)
	}

	fun saveArtist(artist: Artist){
		musicRepository.saveModel(artist)
	}

	fun saveSong(song: Song){
		musicRepository.saveModel(song)
	}
}
