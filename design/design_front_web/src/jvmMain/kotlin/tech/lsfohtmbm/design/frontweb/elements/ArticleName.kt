package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.articleName(
    text: String
) {
    p("articleName") {
        +text
    }
}
