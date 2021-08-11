package com.github.cclassen.oakdsl.model.meta

import com.github.cclassen.oakdsl.model.YamlMapBase
import com.github.cclassen.oakdsl.serialize.YamlSerializer

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