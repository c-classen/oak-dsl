package com.github.cclassen.oakdsl.builder

import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface ParameterReceiver {

    @OptIn(ExperimentalStdlibApi::class)
    fun boolParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<Boolean>(), required, description, content)

    @OptIn(ExperimentalStdlibApi::class)
    fun byteParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<Byte>(), required, description, content)
    @OptIn(ExperimentalStdlibApi::class)
    fun shortParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<Short>(), required, description, content)

    @OptIn(ExperimentalStdlibApi::class)
    fun intParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<Int>(), required, description, content)

    @OptIn(ExperimentalStdlibApi::class)
    fun longParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<Long>(), required, description, content)

    @OptIn(ExperimentalStdlibApi::class)
    fun floatParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<Float>(), required, description, content)

    @OptIn(ExperimentalStdlibApi::class)
    fun doubleParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<Double>(), required, description, content)

    @OptIn(ExperimentalStdlibApi::class)
    fun strParam(
        name: String,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ) = param(name, typeOf<String>(), required, description, content)

    fun param(
        name: String,
        type: KType,
        required: Boolean = true,
        description: String? = null,
        content: (ParameterBuilder.() -> Unit)? = null
    ): ParameterBuilder
}