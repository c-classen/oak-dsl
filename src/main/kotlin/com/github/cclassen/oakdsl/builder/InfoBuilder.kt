package com.github.cclassen.oakdsl.builder

import com.github.cclassen.oakdsl.model.meta.Info

class InfoBuilder(
    val info: Info
) {

    var description by info::description

    var version by info::version

    var title by info::title

    fun contact(content: ContactBuilder.() -> Unit) {
        val contactBuilder = ContactBuilder()
        content(contactBuilder)
        info.contact = contactBuilder.contact
    }
}