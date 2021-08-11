package com.github.cclassen.oakdsl.model.schema

import com.github.cclassen.oakdsl.serialize.YamlSerializer

class ObjectSchema: Schema() {

    var properties: MutableList<ObjectProperty> = mutableListOf()

    var additionalProperties: Schema? = null

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("type", "object")
        val requiredProps = properties.filter { it.required }
        if (requiredProps.isNotEmpty()) {
            serializer.shortStringArray("required", requiredProps.map { it.name })
        }
        if (properties.isNotEmpty()) {
            serializer.entry("properties") {
                for (property in properties) {
                    serializer.entry(property.name) {
                        property.schema.serialize(serializer)
                    }
                }
            }
        }
        additionalProperties?.let {
            serializer.entry("additionalProperties") {
                it.serialize(serializer)
            }
        }
    }
}