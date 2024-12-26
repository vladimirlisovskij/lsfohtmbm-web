package tech.lsfohtmbm.page.frontarticlelist

import kotlinx.html.HTML
import tech.lsfohtmbm.design.frontweb.defaultPage
import tech.lsfohtmbm.design.frontweb.elements.preview
import tech.lsfohtmbm.entity.storage.FinishedPreviews

fun HTML.articleListPage(previews: FinishedPreviews) {
    defaultPage("Блог") {
        previews.previews.forEach { preview ->
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
