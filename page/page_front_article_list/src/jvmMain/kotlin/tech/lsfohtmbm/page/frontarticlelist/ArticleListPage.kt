package tech.lsfohtmbm.page.frontarticlelist

import kotlinx.html.HTML
import tech.lsfohtmbm.design.frontweb.defaultPage
import tech.lsfohtmbm.design.frontweb.elements.preview
import tech.lsfohtmbm.entity.storage.ArticlePreview

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
