package tech.lsfohtmbm.articlerenderer

import kotlinx.html.HTML
import tech.lsfohtmbm.article.*
import tech.lsfohtmbm.design.defaultPage
import tech.lsfohtmbm.design.elements.*

fun HTML.renderArticle(article: Article) {
    defaultPage(article.title) {
        with(article.date) { articleDate(year, month, day) }
        articleName(article.title)
        article.paragraphs.forEach {
            when (it.type) {
                ParagraphType.PRIMARY_HEADER -> primaryHeader(it.value)
                ParagraphType.SECONDARY_HEADER -> secondaryHeader(it.value)
                ParagraphType.TEXT -> defaultText(it.value)
                ParagraphType.IMAGE -> picture(it.value, null)
                ParagraphType.DESCRIPTION -> description(it.value)
            }
        }
    }
}
