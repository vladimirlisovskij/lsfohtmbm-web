package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.img

fun FlowContent.picture(src: String, description: String?) {
    img(
        src = "/static/$src",
        alt = description,
        classes = "picture"
    )
}
