package io.github.cclassen.oakdsl.model.schema

import io.github.cclassen.oakdsl.serialize.YamlSerializer

class ArraySchema(
    var items: Schema
): Schema() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("type", "array")
        serializer.entry("items") {
            items.serialize(serializer)
        }
    }
}