package com.github.cclassen.oakdsl.builder

import com.github.cclassen.oakdsl.model.endpoint.Parameter

class ParameterBuilder(
    val parameter: Parameter
) {

    var description by parameter::description
}
