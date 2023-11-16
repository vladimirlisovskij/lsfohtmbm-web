package tech.lsfohtmbm.page.articlelist

import kotlinx.html.HTML
import tech.lsfohtmbm.entity.article.ArticlePreview
import tech.lsfohtmbm.design.blogweb.defaultPage
import tech.lsfohtmbm.design.blogweb.elements.preview

fun HTML.articleListPage(previews: List<ArticlePreview>) {
    defaultPage("Блог") {
        previews.forEach { preview ->
            preview(
                name = preview.title,
                year = preview.date.year,
                month = preview.date.month,
                day = preview.date.day,
                id = preview.id
            )
        }
    }
}
