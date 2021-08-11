package com.github.cclassen.oakdsl.builder

import com.github.cclassen.oakdsl.model.endpoint.Endpoint
import com.github.cclassen.oakdsl.model.Path
import com.github.cclassen.oakdsl.model.endpoint.BaseParameter
import com.github.cclassen.oakdsl.model.endpoint.BodyContent
import com.github.cclassen.oakdsl.model.endpoint.response.Response
import com.github.cclassen.oakdsl.model.schema.Schema
import java.lang.RuntimeException

class EndpointPathBuilder(
    val method: String,
    operationId: String,
    returnType: Schema?,
    contentType: String,
    private val openApiBuilder: OpenApiBuilder
) {

    private val endpoint = Endpoint(operationId)

    private var path: String  = ""

    init {
        val response = Response("Success")
        if (returnType != null) {
            response.content = BodyContent(contentType, returnType)
        }
        endpoint.responses["200"] = response
    }

    operator fun div(segment: String): EndpointPathBuilder {
        path += "/$segment"
        return this
    }

    operator fun div(parameter: BaseParameter): EndpointPathBuilder {
        path += "/{${parameter.name}}"
        endpoint.parameters.add(parameter)
        return this
    }

    operator fun div(parameterBuilder: ParameterBuilder): EndpointPathBuilder {
        parameterBuilder.parameter.kind = "path"
        path += "/{${parameterBuilder.parameter.name}}"
        endpoint.parameters.add(parameterBuilder.parameter)
        return this
    }

    infix fun def(content: EndpointBuilder.() -> Unit) {
        val endpointBuilder = EndpointBuilder(endpoint, openApiBuilder.components)

        content(endpointBuilder)

        val endpointItem = EndpointItem(method, path, endpoint)

        for (filter in openApiBuilder.endpointFilters) {
            filter.filter(endpointItem)
        }

        val pathObj = openApiBuilder.document.paths.computeIfAbsent(endpointItem.path) { Path() }
        if (pathObj.endpoints.contains(endpointItem.method)) {
            throw RuntimeException("Endpoint ${endpointItem.method} ${endpointItem.path} already exists")
        }
        pathObj.endpoints[endpointItem.method] = endpointItem.endpoint
    }
}

