package io.github.cclassen.oakdsl.serialize.custom

import io.github.cclassen.oakdsl.serialize.YamlSerializable

class YamlArrayBuilder {

    val items: MutableList<YamlSerializable> = mutableListOf()

    fun map(content: YamlMapBuilder.() -> Unit) {
        val builder = YamlMapBuilder()
        content(builder)
        items += YamlMap(builder.entries)
    }

    fun string(value: String) {
        items += YamlString(value)
    }

    fun value(value: String) {
        items += YamlValue(value)
    }

    fun boolean(value: Boolean) {
        items += YamlValue(if (value) "true" else "false")
    }

    fun array(content: YamlArrayBuilder.() -> Unit) {
        val builder = YamlArrayBuilder()
        content(builder)
        items += YamlArray(builder.items)
    }

    /**
     * Values are converted to strings, but printed without quotation marks.
     * For more fine grained control, use shortValueArray.
     */
    fun shortArray(values: List<Any>) {
        items += YamlShortArray(values.map { YamlValue(values.toString()) })
    }

    fun shortValueArray(values: List<YamlValue>) {
        items += YamlShortArray(values)
    }

    fun shortStringArray(values: List<String>) {
        items += YamlShortArray(values.map { YamlString(it) })
    }
}