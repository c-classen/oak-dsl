package com.github.cclassen.oakdsl.builder

import com.github.cclassen.oakdsl.model.endpoint.response.Response

class ResponseBuilder(
    val response: Response
) {
    var description by response::description
}