package io.github.cclassen.oakdsl.model.endpoint

import io.github.cclassen.oakdsl.model.YamlMapBase
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class RequestBody(
    var description: String?,
    var content: BodyContent,
    var required: Boolean = true
): YamlMapBase() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.boolean("required", required)
        serializer.string("description", description)
        serializer.entry("content") {
            content.serialize(serializer)
        }
    }
}