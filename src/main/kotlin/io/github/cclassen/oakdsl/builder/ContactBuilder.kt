package io.github.cclassen.oakdsl.builder

import io.github.cclassen.oakdsl.model.meta.Contact

class ContactBuilder {

    val contact: Contact = Contact()

    var email by contact::email

    var name by contact::name

    var url by contact::url
}
