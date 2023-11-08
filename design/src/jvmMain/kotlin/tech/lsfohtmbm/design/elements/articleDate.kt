package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.p

fun FlowContent.articleDate(
    year: Int,
    month: Int,
    day: Int
) {
    p("articleDate") {
        + "$day/$month/$year"
    }
}
