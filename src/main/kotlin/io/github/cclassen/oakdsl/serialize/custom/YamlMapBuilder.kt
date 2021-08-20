package io.github.cclassen.oakdsl.serialize.custom

import io.github.cclassen.oakdsl.serialize.YamlSerializable

class YamlMapBuilder {

    val entries: MutableMap<String, YamlSerializable> = sortedMapOf()

    fun map(key: String, content: YamlMapBuilder.() -> Unit) {
        val builder = YamlMapBuilder()
        content(builder)
        entries[key] = YamlMap(builder.entries)
    }

    fun string(key: String, value: String) {
        entries[key] = YamlString(value)
    }

    fun value(key: String, value: String) {
        entries[key] = YamlValue(value)
    }

    fun boolean(key: String, value: Boolean) {
        entries[key] = YamlValue(if (value) "true" else "false")
    }

    fun array(key: String, content: YamlArrayBuilder.() -> Unit) {
        val builder = YamlArrayBuilder()
        content(builder)
        entries[key] = YamlArray(builder.items)
    }

    /**
     * Values are converted to strings, but printed without quotation marks.
     * For more fine grained control, use shortValueArray.
     */
    fun shortArray(key: String, values: List<Any>) {
        entries[key] = YamlShortArray(values.map { YamlValue(it.toString()) })
    }

    fun shortValueArray(key: String, values: List<YamlValue>) {
        entries[key] = YamlShortArray(values)
    }

    fun shortStringArray(key: String, values: List<String>) {
        entries[key] = YamlShortArray(values.map { YamlString(it) })
    }
}