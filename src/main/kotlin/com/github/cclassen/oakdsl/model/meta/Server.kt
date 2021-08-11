package com.github.cclassen.oakdsl.model.meta

import com.github.cclassen.oakdsl.model.YamlMapBase
import com.github.cclassen.oakdsl.serialize.YamlSerializer

class Server(
    val url: String,
    val description: String? = null
): YamlMapBase() {

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("url", url)
        serializer.string("description", description)
    }
}
