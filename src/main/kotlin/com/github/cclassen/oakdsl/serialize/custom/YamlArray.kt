package com.github.cclassen.oakdsl.serialize.custom

import com.github.cclassen.oakdsl.serialize.YamlSerializable
import com.github.cclassen.oakdsl.serialize.YamlSerializer

class YamlArray(
    private val items: List<YamlSerializable>
): YamlSerializable {

    override fun serialize(serializer: YamlSerializer) {
        for (item in items) {
            serializer.startArrayItem()
            item.serialize(serializer)
        }
    }

    companion object {

        fun build(content: YamlArrayBuilder.() -> Unit): YamlArray {
            val builder = YamlArrayBuilder()
            content(builder)
            return YamlArray(builder.items)
        }
    }
}