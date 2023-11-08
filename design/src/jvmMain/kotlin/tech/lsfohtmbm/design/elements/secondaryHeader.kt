package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.secondaryHeader(
    text: String
) {
    p("secondaryHeader") {
        + text
    }
}
