package io.github.cclassen.oakdsl.builder

import io.github.cclassen.oakdsl.model.endpoint.response.Response

class ResponseBuilder(
    val response: Response
) {
    var description by response::description
}