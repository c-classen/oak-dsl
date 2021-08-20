package io.github.cclassen.oakdsl.model.meta

import io.github.cclassen.oakdsl.model.YamlMapBase
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Security: YamlMapBase() {

    override fun serializeFixed(serializer: YamlSerializer) {}

}
