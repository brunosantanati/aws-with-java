package me.brunosantana.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*

@DynamoDbBean
abstract class DynamoBaseModel(
    @get:DynamoDbIgnore
    val pkType: String,
    @get:DynamoDbIgnore
    val pkId: String,
    @get:DynamoDbSortKey
    var sk: String,
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["gsi1"])
    var gsi1pk: String,
    @get:DynamoDbSecondarySortKey(indexNames = ["gsi1"])
    var gsi1sk: String,
    @get:DynamoDbAttribute(value = "VersionTimestamp")
    var versionTimestamp: String? = null
) {
    @DynamoDbPartitionKey
    @JsonIgnore
    fun getPk(): String {
        return "${pkType}#${pkId}"
    }

    @JsonProperty
    fun setPk(pk: String) {
        // Do nothing, this is a derived attribute
    }
}