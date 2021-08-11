package com.github.cclassen.oakdsl.model.endpoint

import com.github.cclassen.oakdsl.model.YamlMapBase

abstract class BaseParameter: YamlMapBase() {

    abstract val name: String
}