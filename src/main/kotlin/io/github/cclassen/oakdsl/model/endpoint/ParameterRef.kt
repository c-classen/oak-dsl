package io.github.cclassen.oakdsl.model.endpoint

import io.github.cclassen.oakdsl.serialize.YamlSerializer

class ParameterRef(
    val ref: String,
    override val name: String
): BaseParameter() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("\$ref", ref)
    }
}