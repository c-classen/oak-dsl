package com.github.cclassen.oakdsl.builder

import com.github.cclassen.oakdsl.model.endpoint.Endpoint

class EndpointItem(
    var method: String,
    var path: String,
    val endpoint: Endpoint
)