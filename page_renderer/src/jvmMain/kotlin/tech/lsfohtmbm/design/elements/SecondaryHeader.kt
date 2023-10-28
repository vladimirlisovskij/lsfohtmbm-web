package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.div
import tech.lsfohtmbm.article.SecondaryHeader

fun FlowContent.renderSecondaryHeader(
    header: SecondaryHeader
) {
    div(classes = "secondaryHeader") {
        + header.header
    }
}