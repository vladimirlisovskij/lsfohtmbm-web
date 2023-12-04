package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Long,
    val title: String,
    val date: DateWrapper,
    val paragraphs: List<Paragraph>
)

@Serializable
data class Paragraph(
    val type: ParagraphType,
    val value: String
)

@Serializable
enum class ParagraphType {
    PRIMARY_HEADER,
    SECONDARY_HEADER,
    TEXT,
    IMAGE,
    DESCRIPTION
}
