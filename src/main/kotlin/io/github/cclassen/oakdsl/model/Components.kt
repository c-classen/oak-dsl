package io.github.cclassen.oakdsl.model

import io.github.cclassen.oakdsl.model.endpoint.Parameter
import io.github.cclassen.oakdsl.model.endpoint.response.Response
import io.github.cclassen.oakdsl.model.schema.Schema
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Components: YamlMapBase() {

    val schemas: MutableMap<String, Schema> = sortedMapOf()

    val parameters: MutableMap<String, Parameter> = sortedMapOf()

    val responses: MutableMap<String, Response> = sortedMapOf()

    override fun serializeFixed(serializer: YamlSerializer) {
        if (schemas.isNotEmpty()) {
            serializer.entry("schemas") {
                for ((name, schema) in schemas) {
                    serializer.entry(name) {
                        schema.serialize(serializer)
                    }
                }
            }
        }
        if (parameters.isNotEmpty()) {
            serializer.entry("parameters") {
                for ((name, parameter) in parameters) {
                    serializer.entry(name) {
                        parameter.serialize(serializer)
                    }
                }
            }
        }
        if (responses.isNotEmpty()) {
            serializer.entry("responses") {
                for ((name, response) in responses) {
                    serializer.entry(name) {
                        response.serialize(serializer)
                    }
                }
            }
        }
    }
}