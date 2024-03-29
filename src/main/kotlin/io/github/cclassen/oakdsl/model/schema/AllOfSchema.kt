package io.github.cclassen.oakdsl.model.schema

import io.github.cclassen.oakdsl.serialize.YamlSerializer

class AllOfSchema(
    private val items: MutableList<Schema>
): Schema() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.entry("allOf") {
            for (item in items) {
                serializer.startArrayItem()
                item.serialize(serializer)
            }
        }
    }
}