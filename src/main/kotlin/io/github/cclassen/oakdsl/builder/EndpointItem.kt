package io.github.cclassen.oakdsl.builder

import io.github.cclassen.oakdsl.model.endpoint.Endpoint

class EndpointItem(
    var method: String,
    var path: String,
    val endpoint: Endpoint
)