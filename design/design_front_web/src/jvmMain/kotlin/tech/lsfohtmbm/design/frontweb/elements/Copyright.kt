package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.footer
import kotlinx.html.p

fun FlowContent.copyright() {
    footer {
        p {
            +"© 2023 lsfohtmbm.tech"
        }
    }
}
