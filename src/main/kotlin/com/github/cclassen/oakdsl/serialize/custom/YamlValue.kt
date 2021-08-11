package com.github.cclassen.oakdsl.serialize.custom

import com.github.cclassen.oakdsl.serialize.YamlSerializer

class YamlValue(
    private val value: String
): YamlOneLiner {

    override fun serialize(serializer: YamlSerializer) {
        serializer.value(value)
    }
}