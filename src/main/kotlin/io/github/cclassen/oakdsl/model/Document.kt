package io.github.cclassen.oakdsl.model

import io.github.cclassen.oakdsl.model.meta.Info
import io.github.cclassen.oakdsl.model.meta.Server
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Document: YamlMapBase() {

    val info: Info = Info()
    val servers: MutableList<Server> = mutableListOf()
    val paths: MutableMap<String, Path> = sortedMapOf()
    val components: Components = Components()

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.startDocument("openapi", "3.0.3")
        serializer.entry("info") {
            info.serialize(serializer)
        }
        if (servers.isNotEmpty()) {
            serializer.entry("servers") {
                for (server in servers) {
                    serializer.startArrayItem()
                    server.serialize(serializer)
                }
            }
        }
        serializer.entry("paths") {
            for ((key, endpoint) in paths) {
                serializer.entry(key) {
                    endpoint.serialize(serializer)
                }
            }
        }
        serializer.entry("components") {
            components.serialize(serializer)
        }
    }
}