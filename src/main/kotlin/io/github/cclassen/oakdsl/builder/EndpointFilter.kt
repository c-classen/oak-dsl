package io.github.cclassen.oakdsl.builder

class EndpointFilter(
    val filter: EndpointItem.() -> Unit,
    private val openApiBuilder: OpenApiBuilder
) {
    operator fun invoke(content: () -> Unit) {
        openApiBuilder.startEndpointFilter(this)
        content()
        openApiBuilder.stopEndpointFilter(this)
    }

    fun and(filter: EndpointFilter): EndpointFilter {
        return EndpointFilter(
            filter = { filter.filter(this); this@EndpointFilter.filter(this) },
            openApiBuilder = openApiBuilder
        )
    }

    fun and(filter: EndpointFilter, content: () -> Unit) {
        and(filter)(content)
    }
}