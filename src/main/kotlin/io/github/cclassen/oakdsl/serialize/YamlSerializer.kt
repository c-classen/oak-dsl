package io.github.cclassen.oakdsl.serialize

import java.io.Writer

class YamlSerializer(
    private val writer: Writer
) {

    private var indentLevel: Int = 0

    private var startArray: Boolean = false

    private fun newLine() {
        writer.write("\n")
        for (i in 0 until indentLevel) {
            writer.write("  ")
        }
    }

    fun startDocument(key: String, value: String? = null) {
        writer.write(key)
        writer.write(":")
        if (value != null) {
            writer.write(" ")
            writer.write(value)
        } else {
            indentLevel += 1
        }
    }

    fun entry(key: String, value: String) {
        entry(key, value) {}
    }

    fun entry(key: String, inner: () -> Unit) {
        entry(key, null, inner)
    }

    private inline fun entry(key: String, value: String?, inner: (() -> Unit)) {
        if (startArray) {
            indentLevel -= 1
            newLine()
            writer.write("- ")
            indentLevel += 1
            startArray = false
        } else {
            newLine()
        }
        if (key.matches(Regex("^[\\p{L}]+$"))) {
            writer.write(key)
        } else {
            writer.write("\"$key\"")
        }
        writer.write(":")
        if (value != null) {
            writer.write(" ")
            writer.write(value)
        } else {
            indentLevel += 1
            inner()
            indentLevel -= 1
        }
    }

    fun string(key: String, value: String?) {
        if (value != null) {
            if (value.startsWith('\n')) {
                entry(key) {
                    write(" |")
                    val lines = value.split('\n')
                    for (line in lines.subList(1, lines.size)) {
                        newLine()
                        writer.write(line)
                    }
                }
            } else {
                entry(key, escapeString(value))
            }
        }
    }

    fun shortStringArray(key: String, value: List<String>?) {
        if (value != null) {
            entry(key, "[ " + value.joinToString(", ", transform = this::escapeString) + " ]")
        }
    }

    fun shortArray(content: () -> Unit) {
        writer.write(" [")
        content()
        writer.write(" ]")
    }

    private fun escapeString(value: String): String {
        return '"' + value + '"'
    }

    fun startArrayItem() {
        startArray = true
    }

    fun boolean(key: String, value: Boolean?) {
        if (value != null) {
            entry(key, if (value) "true" else "false")
        }
    }

    fun value(value: String) {
        if (startArray) {
            indentLevel -= 1
            newLine()
            writer.write("- ")
            indentLevel += 1
            startArray = false
        } else {
            writer.write(" ")
        }
        writer.write(value)
    }

    fun stringValue(value: String) {
        value(escapeString(value))
    }

    fun write(str: String) {
        writer.write(str)
    }
}