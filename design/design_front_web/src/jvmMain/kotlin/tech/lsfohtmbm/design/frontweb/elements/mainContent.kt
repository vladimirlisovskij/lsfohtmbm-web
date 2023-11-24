package tech.lsfohtmbm.design.frontweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.main

fun FlowContent.mainContent(content: FlowContent.() -> Unit) {
    main {
        content()
    }
}
