package tech.lsfohtmbm.design.blogweb

import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import tech.lsfohtmbm.design.blogweb.config.favicon
import tech.lsfohtmbm.design.blogweb.config.screenTitle
import tech.lsfohtmbm.design.blogweb.config.styles
import tech.lsfohtmbm.design.blogweb.elements.copyright
import tech.lsfohtmbm.design.blogweb.elements.mainContent
import tech.lsfohtmbm.design.blogweb.elements.topBar

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
