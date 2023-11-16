package tech.lsfohtmbm.design.blogweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.description(
    text: String
) {
    p("description") {
        + text
    }
}