package com.github.cclassen.oakdsl.model.endpoint

import com.github.cclassen.oakdsl.model.meta.Security
import com.github.cclassen.oakdsl.model.YamlMapBase
import com.github.cclassen.oakdsl.model.endpoint.response.BaseResponse
import com.github.cclassen.oakdsl.serialize.YamlSerializer

class Endpoint(
    val operationId: String
): YamlMapBase() {

    var parameters: MutableList<BaseParameter> = mutableListOf()
    var description: String? = null
    var tags: MutableList<String> = mutableListOf()
    var security: MutableList<Security> = mutableListOf()
    var requestBody: RequestBody? = null
    var responses: MutableMap<String, BaseResponse> = sortedMapOf()

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("operationId", operationId)
        serializer.string("description", description)
        if (tags.isNotEmpty()) {
            serializer.shortStringArray("tags", tags)
        }
        if (security.isNotEmpty()) {
            serializer.entry("security") {
                for (securityItem in security) {
                    serializer.startArrayItem()
                    securityItem.serialize(serializer)
                }
            }
        }
        if (parameters.isNotEmpty()) {
            serializer.entry("parameters") {
                for (parameter in parameters) {
                    serializer.startArrayItem()
                    parameter.serialize(serializer)
                }
            }
        }
        requestBody?.let {
            serializer.entry("requestBody") {
                it.serialize(serializer)
            }
        }
        serializer.entry("responses") {
            for ((name, response) in responses) {
                serializer.entry(name) {
                    response.serialize(serializer)
                }
            }
        }
    }
}
