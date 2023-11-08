package tech.lsfohtmbm.design.elements

import kotlinx.html.FlowContent
import kotlinx.html.img

fun FlowContent.articlePicture(id: String) {
    img(
        src = "/image/$id",
        classes = "picture"
    )
}