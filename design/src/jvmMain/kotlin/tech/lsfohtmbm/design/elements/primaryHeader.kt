package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.primaryHeader(
    text: String
) {
    p("primaryHeader") {
        + text
    }
}
