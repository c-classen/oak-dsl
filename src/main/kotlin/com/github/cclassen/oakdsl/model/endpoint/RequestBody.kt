package com.github.cclassen.oakdsl.model.endpoint

import com.github.cclassen.oakdsl.model.YamlMapBase
import com.github.cclassen.oakdsl.serialize.YamlSerializer

class RequestBody(
    var description: String?,
    var content: BodyContent,
    var required: Boolean = true
): YamlMapBase() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.boolean("required", required)
        serializer.string("description", description)
        content.serialize(serializer)
    }
}