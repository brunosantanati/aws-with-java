package me.brunosantana.converter

import org.apache.commons.lang3.StringUtils
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.PrimitiveConverter
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.BooleanAttributeConverter
import software.amazon.awssdk.services.dynamodb.model.AttributeValue


class CustomBooleanAttributeConverter : AttributeConverter<Boolean>, PrimitiveConverter<Boolean> {

    private val awsConverter = BooleanAttributeConverter.create()

    override fun transformFrom(booleanValue: Boolean): AttributeValue {
        return if(booleanValue) {
            AttributeValue.builder().n("1").build()
        } else {
            AttributeValue.builder().n("0").build()
        }
    }

    override fun transformTo(attributeValue: AttributeValue): Boolean {
        return if (!StringUtils.isBlank(attributeValue.n())) {
            val booleanValue = attributeValue.n().toInt() != 0
            val corrected = AttributeValue.builder()
                .bool(booleanValue)
                .build()
            awsConverter.transformTo(corrected)
        } else {
            awsConverter.transformTo(attributeValue)
        }
    }

    override fun type(): EnhancedType<Boolean> {
        return awsConverter.type()
    }

    override fun attributeValueType(): AttributeValueType {
        return awsConverter.attributeValueType()
    }

    override fun primitiveType(): EnhancedType<Boolean> {
        return awsConverter.primitiveType()
    }
}