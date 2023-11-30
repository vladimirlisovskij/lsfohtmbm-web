package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.primaryHeader(
    text: String
) {
    p("primaryHeader") {
        +text
    }
}
