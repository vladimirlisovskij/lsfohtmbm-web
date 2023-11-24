package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.p

fun FlowContent.preview(
    name: String,
    year: Int,
    month: Int,
    day: Int,
    id: Long,
) {
    p {
        a(
            classes = "preview",
            href = "article/$id"
        ) {
            + "$day/$month/$year - $name"
        }
    }
}