package io.github.cclassen.oakdsl.model.meta

import io.github.cclassen.oakdsl.model.YamlMapBase
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Contact: YamlMapBase() {

    var email: String? = null
    var name: String? = null
    var url: String? = null

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("email", email)
        serializer.string("name", name)
        serializer.string("url", url)
    }
}