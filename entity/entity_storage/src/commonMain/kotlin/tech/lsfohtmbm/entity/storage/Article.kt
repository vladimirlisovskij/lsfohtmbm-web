package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Long,
    val title: String,
    val date: DateWrapper,
    val ready: Boolean,
    val paragraphs: List<Paragraph>,
) {
    companion object {
        const val NO_ID = -1L
    }
}
