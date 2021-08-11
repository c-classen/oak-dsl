package com.github.cclassen.oakdsl.component

import com.github.cclassen.oakdsl.builder.ResponseBuilder
import com.github.cclassen.oakdsl.model.endpoint.BodyContent
import com.github.cclassen.oakdsl.model.endpoint.Parameter
import com.github.cclassen.oakdsl.model.endpoint.ParameterRef
import com.github.cclassen.oakdsl.model.endpoint.response.Response
import com.github.cclassen.oakdsl.model.endpoint.response.ResponseRef
import com.github.cclassen.oakdsl.model.schema.Schema

class ResponseResolver {

    private val responses: MutableMap<String, Response> = sortedMapOf()

    fun response(
        ref: String,
        description: String,
        schema: Schema? = null,
        contentType: String = "application/json",
        content: (ResponseBuilder.() -> Unit)? = null
    ): ResponseRef {
        if (responses.containsKey(ref)) {
            throw RuntimeException("A response with the given ref \"$ref\" already exists")
        }
        val response = Response(description)
        if (schema != null) {
            response.content = BodyContent(contentType, schema)
        }
        val builder = ResponseBuilder(response)
        content?.let { it(builder) }
        responses[ref] = response
        return ResponseRef("#/components/responses/$ref")
    }

    fun addAllResponsesTo(responses: MutableMap<String, Response>) {
        val existingKey = this.responses.keys.find { responses.containsKey(it) }
        if (existingKey != null) {
            throw RuntimeException("Cannot add registered parameters. Name $existingKey is already in use")
        }
        responses.putAll(this.responses)
    }
}