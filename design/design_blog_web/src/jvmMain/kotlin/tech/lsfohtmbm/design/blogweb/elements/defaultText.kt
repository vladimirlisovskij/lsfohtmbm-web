package tech.lsfohtmbm.design.blogweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.defaultText(
    text: String
) {
    p("defaultText") {
        + text
    }
}
