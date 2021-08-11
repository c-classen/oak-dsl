package com.github.cclassen.oakdsl.model.endpoint

import com.github.cclassen.oakdsl.model.schema.Schema
import com.github.cclassen.oakdsl.serialize.YamlSerializer

class Parameter(
    var kind: String,
    override var name: String,
    var required: Boolean,
    var schema: Schema,
    var description: String? = null
): BaseParameter() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.entry("in", kind)
        serializer.string("name", name)
        serializer.boolean("required", required)
        serializer.string("description", description)
        serializer.entry("schema") {
            schema.serialize(serializer)
        }
    }
}
