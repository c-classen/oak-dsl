package com.github.cclassen.oakdsl.model.schema

class ObjectProperty(
    var name: String,
    var schema: Schema,
    var required: Boolean = true,
    var description: String? = null
)
