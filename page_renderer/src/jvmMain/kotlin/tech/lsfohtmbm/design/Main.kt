package tech.lsfohtmbm.design

import kotlinx.html.HTML
import tech.lsfohtmbm.article.*
import tech.lsfohtmbm.design.elements.primaryHeader
import tech.lsfohtmbm.design.elements.renderSecondaryHeader
import tech.lsfohtmbm.design.elements.renderText

fun HTML.render(article: Article) {
    defaultPage(article.title) {
        article.paragraphs.forEach {
            when (it) {
                is PrimaryHeader -> primaryHeader(it.header)
                is SecondaryHeader -> renderSecondaryHeader(it)
                is Text -> renderText(it)
                is Image -> TODO()
            }
        }
    }
}
