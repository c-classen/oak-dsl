package io.github.cclassen.oakdsl.model.endpoint

import io.github.cclassen.oakdsl.model.YamlMapBase

abstract class BaseParameter: YamlMapBase() {

    abstract val name: String
}