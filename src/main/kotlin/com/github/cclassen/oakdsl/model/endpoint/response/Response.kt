package com.github.cclassen.oakdsl.model.endpoint.response

import com.github.cclassen.oakdsl.model.endpoint.BodyContent
import com.github.cclassen.oakdsl.serialize.YamlSerializer

class Response(
    var description: String,
    var content: BodyContent? = null
): BaseResponse() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("description", description)
        content?.serializeFixed(serializer)
    }
}
