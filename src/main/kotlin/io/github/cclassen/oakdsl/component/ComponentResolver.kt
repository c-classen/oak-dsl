package io.github.cclassen.oakdsl.component

import io.github.cclassen.oakdsl.builder.ResponseBuilder
import io.github.cclassen.oakdsl.model.Components
import io.github.cclassen.oakdsl.model.endpoint.ParameterRef
import io.github.cclassen.oakdsl.model.endpoint.response.ResponseRef
import io.github.cclassen.oakdsl.model.schema.*
import kotlin.reflect.KClass
import kotlin.reflect.KType

class ComponentResolver {

    val typeResolver = TypeResolver()

    val parameterResolver = ParameterResolver()

    val responseResolver = ResponseResolver()

    fun resolveType(type: KType): Schema {
        return typeResolver.resolveType(type)
    }

    fun resolveClass(type: KClass<*>): Schema {
        return typeResolver.resolveKClass(type)
    }

    fun type(ref: String, schema: Schema): Schema {
        return typeResolver.type(ref, schema)
    }

    fun parameter(ref: String, name: String, schema: Schema, kind: String = "query", required: Boolean = true, description: String? = null): ParameterRef {
        return parameterResolver.parameter(ref, name, schema, kind, required, description)
    }

    fun response(
        ref: String,
        description: String,
        schema: Schema? = null,
        contentType: String = "application/json",
        content: (ResponseBuilder.() -> Unit)? = null
    ): ResponseRef {
        return responseResolver.response(ref, description, schema, contentType, content)
    }

    fun addAllTo(components: Components) {
        typeResolver.addAllSchemasTo(components.schemas)
        parameterResolver.addAllParametersTo(components.parameters)
        responseResolver.addAllResponsesTo(components.responses)
    }
}