package tech.lsfohtmbm.design.blogweb.elements

import kotlinx.html.FlowContent
import kotlinx.html.main

fun FlowContent.mainContent(content: FlowContent.() -> Unit) {
    main {
        content()
    }
}
