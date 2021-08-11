package com.github.cclassen.oakdsl.serialize.custom

import com.github.cclassen.oakdsl.serialize.YamlSerializable
import com.github.cclassen.oakdsl.serialize.YamlSerializer

class YamlShortArray(
    private val values: List<YamlOneLiner>
): YamlSerializable {

    override fun serialize(serializer: YamlSerializer) {
        serializer.shortArray {
            for ((i, value) in values.withIndex()) {
                if (i != 0) {
                    serializer.write(",")
                }
                value.serialize(serializer)
            }
        }
    }
}