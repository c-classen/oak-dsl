package io.github.cclassen.oakdsl.component

import io.github.cclassen.oakdsl.model.endpoint.Parameter
import io.github.cclassen.oakdsl.model.endpoint.ParameterRef
import io.github.cclassen.oakdsl.model.schema.Schema

class ParameterResolver {

    private val parameters: MutableMap<String, Parameter> = sortedMapOf()

    fun parameter(
        ref: String,
        name: String,
        schema: Schema,
        kind: String = "query",
        required: Boolean = true,
        description: String? = null
    ): ParameterRef {
        if (parameters.containsKey(ref)) {
            throw RuntimeException("A parameter with ref \"$ref\" has already been registered")
        }
        parameters[ref] = Parameter(kind, name, required, schema, description)
        return ParameterRef("#/components/parameters/$ref", name)
    }

    fun addAllParametersTo(parameters: MutableMap<String, Parameter>) {
        val existingKey = this.parameters.keys.find { parameters.containsKey(it) }
        if (existingKey != null) {
            throw RuntimeException("Cannot add registered parameters. Name $existingKey is already in use")
        }
        parameters.putAll(this.parameters)
    }
}