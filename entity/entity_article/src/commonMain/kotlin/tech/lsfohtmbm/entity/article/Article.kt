package tech.lsfohtmbm.entity.article

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Long,
    val title: String,
    val date: DateWrapper,
    val paragraphs: List<Paragraph>
)

