package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.p


fun FlowContent.articleName(
    text: String
) {
    p("articleName") {
        + text
    }
}
