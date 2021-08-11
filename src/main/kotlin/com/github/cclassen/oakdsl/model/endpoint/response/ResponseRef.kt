package com.github.cclassen.oakdsl.model.endpoint.response

import com.github.cclassen.oakdsl.serialize.YamlSerializer

class ResponseRef(
    var ref: String
): BaseResponse() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("\$ref", ref)
    }
}