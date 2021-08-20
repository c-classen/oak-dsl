package io.github.cclassen.oakdsl.serialize.custom

import io.github.cclassen.oakdsl.serialize.YamlSerializer

class YamlValue(
    private val value: String
): YamlOneLiner {

    override fun serialize(serializer: YamlSerializer) {
        serializer.value(value)
    }
}