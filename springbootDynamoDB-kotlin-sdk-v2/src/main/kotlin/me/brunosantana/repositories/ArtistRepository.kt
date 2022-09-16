package me.brunosantana.repositories

import me.brunosantana.DateUtils
import me.brunosantana.dto.Artist
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class ArtistRepository(
    val enhancedClient: DynamoDbEnhancedClient
) {

    fun save(artist: Artist, existingVersion: String?, versioningCheck: Boolean){
        println(existingVersion)

        val artistTable: DynamoDbTable<Artist> =
            enhancedClient.table("music", TableSchema.fromBean(Artist::class.java))

        if(versioningCheck){
            if(existingVersion == null){
                artistTable.putItem(artist)
            }else{

                val incomingDate = DateUtils.convertStringToZonedDateTime(artist.versionTimestamp!!)
                val existingDate = DateUtils.convertStringToZonedDateTime(existingVersion)

                if(DateUtils.isIncomingDateNewer(incomingDate, existingDate)){ //check versioning dates
                    artistTable.putItem(artist) //check how to override properly
                }else{
                    println("Skip. ${artist.versionTimestamp} is older than $existingVersion")
                }

            }
        }else{
            artistTable.putItem(artist)
        }
    }

}