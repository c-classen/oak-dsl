package com.github.cclassen.oakdsl

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.github.cclassen.oakdsl.builder.OpenApiBuilder
import java.io.StringWriter

abstract class TestBase {

    fun build(content: OpenApiBuilder.() -> Unit): JsonNode {
        val writer = StringWriter()
        OpenApiBuilder.write(writer, content)
        val yamlStr = writer.buffer.toString()
        val objectMapper = ObjectMapper(YAMLFactory())
        return objectMapper.readTree(yamlStr)
    }

    interface TestObj {
        val text: String
    }

    fun extractEndpoint(yaml: JsonNode, method: String, path: String): JsonNode {
        return yaml["paths"][path][method]
    }

    fun extractDefaultResponse(endpoint: JsonNode): JsonNode {
        return endpoint["responses"]["200"]
    }

    fun extractResponseSchema(response: JsonNode, contentType: String = "application/json"): JsonNode {
        return response["content"][contentType]["schema"]
    }
}