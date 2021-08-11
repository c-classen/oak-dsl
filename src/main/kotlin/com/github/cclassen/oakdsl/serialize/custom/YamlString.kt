package com.github.cclassen.oakdsl.serialize.custom

import com.github.cclassen.oakdsl.serialize.YamlSerializer

class YamlString(
    private val text: String
): YamlOneLiner {

    override fun serialize(serializer: YamlSerializer) {
        serializer.stringValue(text)
    }
}