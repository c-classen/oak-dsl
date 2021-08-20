package io.github.cclassen.oakdsl.builder

import io.github.cclassen.oakdsl.model.endpoint.Parameter

class ParameterBuilder(
    val parameter: Parameter
) {

    var description by parameter::description
}
