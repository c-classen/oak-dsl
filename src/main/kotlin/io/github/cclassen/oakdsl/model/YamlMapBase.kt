package io.github.cclassen.oakdsl.model

import io.github.cclassen.oakdsl.serialize.YamlSerializable
import io.github.cclassen.oakdsl.serialize.YamlSerializer
import kotlin.reflect.KClass

abstract class YamlMapBase: YamlSerializable {

    var additionalYamlProperties: MutableMap<String, YamlSerializable> = sortedMapOf()

    val markers: MutableMap<KClass<*>, Any?> = mutableMapOf()

    abstract fun serializeFixed(serializer: YamlSerializer)

    override fun serialize(serializer: YamlSerializer) {
        serializeFixed(serializer)
        for ((key, value) in additionalYamlProperties) {
            serializer.entry(key) {
                value.serialize(serializer)
            }
        }
    }

    fun mark(value: Any) {
        markers[value::class] = value
    }

    fun isMarked(type: KClass<*>): Boolean {
        return markers.containsKey(type)
    }

    inline fun <reified T> isMarked(): Boolean {
        return isMarked(T::class)
    }

    fun getMark(type: KClass<*>): Any? {
        return markers[type]
    }

    inline fun <reified T> getMark(): T? {
        return getMark(T::class) as? T
    }
}