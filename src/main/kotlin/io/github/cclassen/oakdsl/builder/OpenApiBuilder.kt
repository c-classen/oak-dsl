package io.github.cclassen.oakdsl.builder

import io.github.cclassen.oakdsl.model.Document
import io.github.cclassen.oakdsl.model.endpoint.Parameter
import io.github.cclassen.oakdsl.model.meta.Server
import io.github.cclassen.oakdsl.model.schema.Schema
import io.github.cclassen.oakdsl.serialize.YamlSerializer
import io.github.cclassen.oakdsl.component.ComponentResolver
import java.io.File
import java.io.StringWriter
import java.io.Writer
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class OpenApiBuilder: ParameterReceiver {

    val components = ComponentResolver()

    val document = Document()

    val endpointFilters: MutableList<EndpointFilter> = mutableListOf()

    val globalFilters: MutableList<EndpointFilter> = mutableListOf<EndpointFilter>()

    fun info(content: InfoBuilder.() -> Unit) {
        val infoBuilder = InfoBuilder(document.info)
        content(infoBuilder)
    }

    fun server(url: String, description: String? = null) {
        document.servers.add(Server(url, description))
    }

    fun post(operationId: String): EndpointPathBuilder {
        return endpoint("post", operationId)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R: Any> post(
        operationId: String,
        contentType: String = "application/json",
        returnType: KType = typeOf<R>()
    ): EndpointPathBuilder {
        return endpoint<R>("post", operationId, contentType, returnType)
    }

    fun get(operationId: String): EndpointPathBuilder {
        return endpoint("get", operationId)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R: Any> get(
        operationId: String,
        contentType: String = "application/json",
        returnType: KType = typeOf<R>()
    ): EndpointPathBuilder {
        return endpoint<R>("get", operationId, contentType, returnType)
    }

    fun put(operationId: String): EndpointPathBuilder {
        return endpoint("put", operationId)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R: Any> put(
        operationId: String,
        contentType: String = "application/json",
        returnType: KType = typeOf<R>()
    ): EndpointPathBuilder {
        return endpoint<R>("put", operationId, contentType, returnType)
    }

    fun patch(operationId: String): EndpointPathBuilder {
        return endpoint("patch", operationId)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R: Any> patch(
        operationId: String,
        contentType: String = "application/json",
        returnType: KType = typeOf<R>()
    ): EndpointPathBuilder {
        return endpoint<R>("patch", operationId, contentType, returnType)
    }

    fun delete(operationId: String): EndpointPathBuilder {
        return endpoint("delete", operationId)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R> delete(
        operationId: String,
        contentType: String = "application/json",
        returnType: KType = typeOf<R>()
    ): EndpointPathBuilder {
        return endpoint<R>("delete", operationId, contentType, returnType)
    }

    fun options(operationId: String): EndpointPathBuilder {
        return endpoint("options", operationId)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R> options(
        operationId: String,
        contentType: String = "application/json",
        returnType: KType = typeOf<R>()
    ): EndpointPathBuilder {
        return endpoint<R>("options", operationId, contentType, returnType)
    }

    fun endpoint(method: String, operationId: String): EndpointPathBuilder {
        return endpoint<Unit>(method, operationId)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R> endpoint(
        method: String,
        operationId: String,
        contentType: String = "application/json",
        returnType: KType = typeOf<R>()
    ): EndpointPathBuilder {
        return endpointDynamic(method, operationId, contentType, returnType)
    }

    fun endpointDynamic(
        method: String,
        operationId: String,
        contentType: String,
        returnType: KType
    ): EndpointPathBuilder {
        val schema = if (returnType.classifier == Unit::class) {
            null
        } else {
            try {
                components.resolveType(returnType)
            } catch (ex: Exception) {
                throw RuntimeException("Failed to resolve return type of endpoint $operationId", ex)
            }
        }
        return EndpointPathBuilder(method, operationId, schema, contentType, this)
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified R> param(
        name: String,
        required: Boolean = true,
        description: String? = null,
        kind: String? = null,
        noinline content: (ParameterBuilder.() -> Unit)? = null
    ): ParameterBuilder {
        return param(name, typeOf<R>(), required, description, kind, content)
    }

    override fun param(
        name: String,
        type: KType,
        required: Boolean,
        description: String?,
        kind: String?,
        content: (ParameterBuilder.() -> Unit)?
    ): ParameterBuilder {
        val parameter = Parameter(kind ?: "path", name, required, components.resolveType(type), description)
        val builder = ParameterBuilder(parameter)
        content?.let { it(builder) }
        return builder
    }

    inline fun <reified T> customResolve(noinline resolver: (type: KClass<*>) -> Schema) {
        components.typeResolver.customClassResolvers[T::class.java] = resolver
    }

    inline fun <reified T, reified U> customResolve() {
        customResolve<T> { components.resolveClass(U::class) }
    }

    fun globalFilter(filter: EndpointFilter) {
        globalFilters.add(filter)
    }

    fun globalFilter(filter: EndpointItem.() -> Unit) {
        globalFilter(endpointFilter(filter))
    }

    /**
     * Apply the given filter to all endpoints marked with an object of the given type
     */
    fun globalFilterForMarked(markedWith: KClass<*>, filter: EndpointItem.() -> Unit) {
        globalFilter {
            if (endpoint.isMarked(markedWith)) {
                filter(this)
            }
        }
    }

    /**
     * Apply the given filter to all endpoints marked with an object that equals the given one
     */
    fun globalFilterForMarked(markedWith: Any, filter: EndpointItem.() -> Unit) {
        globalFilter {
            if (endpoint.getMark(markedWith::class) == markedWith) {
                filter(this)
            }
        }
    }

    /**
     * Apply the given filter to all endpoints not marked with an object of the given type
     */
    fun globalFilterForNotMarked(notMarkedWith: KClass<*>, filter: EndpointItem.() -> Unit) {
        globalFilter {
            if (!endpoint.isMarked(notMarkedWith)) {
                filter(this)
            }
        }
    }

    fun endpointFilter(filter: EndpointItem.() -> Unit): EndpointFilter {
        return EndpointFilter(filter, this)
    }

    fun endpointFilter(filter: EndpointItem.() -> Unit, content: () -> Unit) {
        endpointFilter(filter)(content)
    }

    fun startEndpointFilter(filter: EndpointFilter) {
        endpointFilters.add(0, filter)
    }

    fun stopEndpointFilter(filter: EndpointFilter) {
        val removed = endpointFilters.removeFirst()
        if (removed != filter) {
            throw RuntimeException("Out of order filter removal")
        }
    }

    fun prefix(pathPrefix: String): EndpointFilter {
        return endpointFilter {
            path = "/" + pathPrefix.removePrefix("/").removeSuffix("/") + path
        }
    }

    fun prefix(pathPrefix: String, content: () -> Unit) {
        prefix(pathPrefix)(content)
    }

    fun tagged(tag: String): EndpointFilter {
        return endpointFilter {
            endpoint.tags.add(tag)
        }
    }

    fun tagged(tag: String, content: () -> Unit) {
        tagged(tag)(content)
    }

    fun marked(value: Any): EndpointFilter {
        return endpointFilter { endpoint.mark(value) }
    }

    fun marked(value: Any, content: () -> Unit) {
        return marked(value)(content)
    }

    companion object {

        fun base(content: OpenApiBuilder.() -> Unit): OpenApiBuilder.() -> Unit {
            return content
        }

        fun file(filename: String, base: OpenApiBuilder.() -> Unit, content: OpenApiBuilder.() -> Unit) {
            File(filename).bufferedWriter().use { writer ->
                write(writer) {
                    base(this)
                    content(this)
                }
            }
        }

        fun file(filename: String, content: OpenApiBuilder.() -> Unit) {
            File(filename).bufferedWriter().use { writer ->
                write(writer, content)
            }
        }

        fun string(content: OpenApiBuilder.() -> Unit): String {
            val writer = StringWriter()
            write(writer, content)
            return writer.toString()
        }

        fun write(writer: Writer, content: OpenApiBuilder.() -> Unit) {
            val builder = OpenApiBuilder()
            content(builder)
            for (filter in builder.globalFilters) {
                builder.document.paths
                    .asSequence()
                    .flatMap { (path, pathObj) -> pathObj.endpoints.asSequence().map { path to it } }
                    .map { (path, endpoint) ->  EndpointItem(endpoint.key, path, endpoint.value) }
                    .forEach { filter.filter(it) }
            }
            builder.components.addAllTo(builder.document.components)
            writer.write("# This file is generated by oak-dsl (https://github.com/c-classen/oak-dsl)\n")
            val serializer = YamlSerializer(writer)
            builder.document.serialize(serializer)
        }
    }
}