package tech.lsfohtmbm.utils.tests.ktor

import kotlinx.html.HTML
import kotlinx.html.html
import kotlinx.html.stream.appendHTML

/**
 * copy-paste from ktor implementation
 */
fun mockHtml(block: HTML.() -> Unit) = buildString {
    append("<!DOCTYPE html>\n")
    appendHTML().html(block = block)
}
