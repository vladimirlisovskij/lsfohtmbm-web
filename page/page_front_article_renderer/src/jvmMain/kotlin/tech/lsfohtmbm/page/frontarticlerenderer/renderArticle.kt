package tech.lsfohtmbm.page.frontarticlerenderer

import kotlinx.html.HTML
import tech.lsfohtmbm.design.frontweb.defaultPage
import tech.lsfohtmbm.design.frontweb.elements.*
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.ParagraphType

fun HTML.renderArticle(article: Article) {
    defaultPage(article.title) {
        with(article.date) { articleDate(year, month, day) }
        articleName(article.title)
        article.paragraphs.forEach {
            when (it.type) {
                ParagraphType.PRIMARY_HEADER -> primaryHeader(it.value)
                ParagraphType.SECONDARY_HEADER -> secondaryHeader(it.value)
                ParagraphType.TEXT -> defaultText(it.value)
                ParagraphType.IMAGE -> articlePicture(it.value)
                ParagraphType.DESCRIPTION -> description(it.value)
            }
        }
    }
}
