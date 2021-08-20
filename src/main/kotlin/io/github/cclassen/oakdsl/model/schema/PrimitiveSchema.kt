package io.github.cclassen.oakdsl.model.schema

import io.github.cclassen.oakdsl.serialize.YamlSerializer

/**
 * A primitive type consisting of a type name and an optional format or an optional list of allowed values
 */
class PrimitiveSchema(
    private val type: String,
    private val format: String? = null,
    private val enum: List<String>? = null
): Schema() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.entry("type", type)
        serializer.string("format", format)
        serializer.shortStringArray("enum", enum)
    }
}