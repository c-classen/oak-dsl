package com.github.cclassen.oakdsl.model.schema

import com.github.cclassen.oakdsl.serialize.YamlSerializer

class SchemaRef(name: String): Schema() {

    var ref: String = "#/components/schemas/$name"

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("\$ref", ref)
    }
}