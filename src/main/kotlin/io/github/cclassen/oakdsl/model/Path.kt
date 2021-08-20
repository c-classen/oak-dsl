package io.github.cclassen.oakdsl.model

import io.github.cclassen.oakdsl.model.endpoint.Endpoint
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Path: YamlMapBase() {

    val endpoints: MutableMap<String, Endpoint> = sortedMapOf()

    override fun serializeFixed(serializer: YamlSerializer) {
        for ((method, endpoint) in endpoints) {
            serializer.entry(method) {
                endpoint.serialize(serializer)
            }
        }
    }
}