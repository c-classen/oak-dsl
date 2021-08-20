package io.github.cclassen.oakdsl.model.meta

import io.github.cclassen.oakdsl.model.YamlMapBase
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Server(
    val url: String,
    val description: String? = null
): YamlMapBase() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("url", url)
        serializer.string("description", description)
    }
}
