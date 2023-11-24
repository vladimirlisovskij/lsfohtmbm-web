package tech.lsfohtmbm.design.frontweb

import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import tech.lsfohtmbm.design.frontweb.config.favicon
import tech.lsfohtmbm.design.frontweb.config.screenTitle
import tech.lsfohtmbm.design.frontweb.config.styles
import tech.lsfohtmbm.design.frontweb.elements.copyright
import tech.lsfohtmbm.design.frontweb.elements.mainContent
import tech.lsfohtmbm.design.frontweb.elements.topBar

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
