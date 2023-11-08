package tech.lsfohtmbm.articlelistpage

import kotlinx.html.HTML
import tech.lsfohtmbm.article.ArticlePreview
import tech.lsfohtmbm.design.defaultPage
import tech.lsfohtmbm.design.elements.preview

fun HTML.articleListPage(previews: List<ArticlePreview>) {
    defaultPage("Список") {
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
