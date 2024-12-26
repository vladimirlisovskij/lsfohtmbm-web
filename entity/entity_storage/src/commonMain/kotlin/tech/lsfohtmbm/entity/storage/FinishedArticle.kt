package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class FinishedArticle(
    val id: Long,
    val title: String,
    val date: DateWrapper,
    val paragraphs: List<Paragraph>,
)
