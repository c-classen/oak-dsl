package io.github.cclassen.oakdsl.model.meta

import io.github.cclassen.oakdsl.model.YamlMapBase
import io.github.cclassen.oakdsl.serialize.YamlSerializer

class Info: YamlMapBase() {

    var description: String? = null
    var version: String? = null
    var title: String? = null
    var contact: Contact? = null

    override fun serializeFixed(serializer: YamlSerializer) {
        serializer.string("description", description)
        serializer.string("version", version)
        serializer.string("title", title)
        contact?.let { contact ->
            serializer.entry("contact") {
                contact.serialize(serializer)
            }
        }
    }
}