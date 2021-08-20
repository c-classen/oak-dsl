package io.github.cclassen.oakdsl.serialize.custom

import io.github.cclassen.oakdsl.serialize.YamlSerializable
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class YamlMap(
    private val entries: Map<String, YamlSerializable>
): YamlSerializable {

    override fun serialize(serializer: YamlSerializer) {
        for (entry in entries) {
            serializer.entry(entry.key) {
                entry.value.serialize(serializer)
            }
        }
    }

    companion object {

        fun build(content: YamlMapBuilder.() -> Unit): YamlMap {
            val builder = YamlMapBuilder()
            content(builder)
            return YamlMap(builder.entries)
        }
    }
}