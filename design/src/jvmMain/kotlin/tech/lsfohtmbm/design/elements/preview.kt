package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.a

fun FlowContent.preview(
    name: String,
    year: Int,
    month: Int,
    day: Int,
    id: Long,
) {
    a(
        classes = "preview",
        href = "article/$id"
    ) {
        + "$day/$month/$year - $name"
    }
}