package io.github.cclassen.oakdsl.model.endpoint.response

import io.github.cclassen.oakdsl.model.endpoint.BodyContent
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Response(
    var description: String,
    var content: BodyContent? = null
): BaseResponse() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("description", description)
        content?.let {
            serializer.entry("content") {
                it.serialize(serializer)
            }
        }
    }
}
