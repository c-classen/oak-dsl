package io.github.cclassen.oakdsl.builder

import io.github.cclassen.oakdsl.component.ComponentResolver
import io.github.cclassen.oakdsl.model.endpoint.*
import io.github.cclassen.oakdsl.model.endpoint.response.BaseResponse
import io.github.cclassen.oakdsl.model.endpoint.response.Response
import io.github.cclassen.oakdsl.model.schema.Schema
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class EndpointBuilder(
    val endpoint: Endpoint,
    val components: ComponentResolver
): ParameterReceiver {

    var description by endpoint::description

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> requestBody(
        contentType: String = "application/json",
        description: String? = null,
        required: Boolean = true
    ) {
        requestBody(contentType, description, required, typeOf<T>())
    }

    fun requestBody(
        contentType: String,
        description: String?,
        required: Boolean,
        type: KType
    ) {
        val schema = try {
            components.resolveType(type)
        } catch (ex: RuntimeException) {
            throw RuntimeException("Failed to resolve type of request body", ex)
        }
        val bodyContent = BodyContent(contentType, schema)
        val requestBody = RequestBody(description, bodyContent, required)
        endpoint.requestBody = requestBody
    }

    fun response(
        code: String,
        description: String = "Success"
    ) {
        response(code, description, null)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> response(
        code: String,
        description: String = "Success",
        contentType: String = "application/json",
        noinline content: (ResponseBuilder.() -> Unit)? = null
    ) {
        val schema = components.resolveType(typeOf<T>())
        response(code, description, schema, contentType, content)
    }

    fun response(
        code: String,
        description: String = "Success",
        schema: Schema? = null,
        contentType: String = "application/json",
        content: (ResponseBuilder.() -> Unit)? = null
    ) {
        val response = Response(description)
        if (schema != null) {
            response.content = BodyContent(contentType, schema)
        }
        val builder = ResponseBuilder(response)
        content?.let { it(builder) }
        endpoint.responses[code] = response
    }

    fun response(code: String, response: BaseResponse) {
        endpoint.responses[code] = response
    }

    fun tags(vararg tags: String) {
        endpoint.tags.addAll(tags)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R> param(
        name: String,
        required: Boolean = true,
        description: String? = null,
        noinline content: (ParameterBuilder.() -> Unit)? = null
    ): ParameterBuilder {
        return param(name, typeOf<R>(), required, description, content)
    }

    override fun param(
        name: String,
        type: KType,
        required: Boolean,
        description: String?,
        content: (ParameterBuilder.() -> Unit)?
    ): ParameterBuilder {
        val parameter = Parameter("query", name, required, components.resolveType(type), description)
        val builder = ParameterBuilder(parameter)
        content?.let { it(builder) }
        endpoint.parameters.add(parameter)
        return builder
    }

    fun params(vararg params: BaseParameter) {
        endpoint.parameters.addAll(params)
    }
}