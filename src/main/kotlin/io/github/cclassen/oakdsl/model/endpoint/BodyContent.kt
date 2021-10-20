package io.github.cclassen.oakdsl.model.endpoint

import io.github.cclassen.oakdsl.model.YamlMapBase
import io.github.cclassen.oakdsl.model.schema.Schema
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class BodyContent(
    var variants: MutableMap<String, Schema>
): YamlMapBase() {

    override fun serializeFixed(serializer: YamlSerializer) {
        for ((contentType, schema) in variants) {
            serializer.entry(contentType) {
                serializer.entry("schema") {
                    schema.serialize(serializer)
                }
            }
        }
    }
}
