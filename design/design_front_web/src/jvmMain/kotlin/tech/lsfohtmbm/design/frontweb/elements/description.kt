package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.description(
    text: String
) {
    p("description") {
        + text
    }
}