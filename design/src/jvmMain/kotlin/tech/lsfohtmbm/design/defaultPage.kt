package tech.lsfohtmbm.design

import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import tech.lsfohtmbm.design.config.favicon
import tech.lsfohtmbm.design.config.screenTitle
import tech.lsfohtmbm.design.config.styles
import tech.lsfohtmbm.design.elements.copyright
import tech.lsfohtmbm.design.elements.mainContent
import tech.lsfohtmbm.design.elements.topBar

fun HTML.defaultPage(
    title: String, content: FlowContent.() -> Unit
) {
    head {
        favicon()
        screenTitle(title)
        styles()
    }

    body {
        topBar()
        mainContent(content)
        copyright()
    }
}
