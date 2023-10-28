package tech.lsfohtmbm.article

data class Article(val title: String, val paragraphs: List<Paragraph>)

sealed interface Paragraph

data class PrimaryHeader(val header: String) : Paragraph

data class SecondaryHeader(val header: String) : Paragraph

data class Text(val text: String) : Paragraph

data class Image(val id: Int, val description: String) : Paragraph