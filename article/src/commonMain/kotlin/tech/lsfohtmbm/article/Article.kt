package tech.lsfohtmbm.article

data class Article(
    val id: Long,
    val title: String,
    val date: DateWrapper,
    val paragraphs: List<Paragraph>
)

data class ArticlePreview(
    val id: Long,
    val title: String,
    val date: DateWrapper
)

data class DateWrapper(
    val day: Int,
    val month: Int,
    val year: Int
)

enum class ParagraphType {
    PRIMARY_HEADER,
    SECONDARY_HEADER,
    TEXT,
    IMAGE,
    DESCRIPTION
}

class Paragraph(
    val type: ParagraphType,
    val value: String
)