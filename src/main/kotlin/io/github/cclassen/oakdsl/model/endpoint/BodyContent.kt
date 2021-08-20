package io.github.cclassen.oakdsl.model.endpoint

import io.github.cclassen.oakdsl.model.YamlMapBase
import io.github.cclassen.oakdsl.model.schema.Schema
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class BodyContent(
    var contentType: String,
    var schema: Schema
): YamlMapBase() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.entry("content") {
            serializer.entry(contentType) {
                serializer.entry("schema") {
                    schema.serialize(serializer)
                }
            }
        }
    }
}
