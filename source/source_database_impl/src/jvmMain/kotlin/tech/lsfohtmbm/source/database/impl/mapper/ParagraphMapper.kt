package tech.lsfohtmbm.source.database.impl.mapper

import tech.lsfohtmbm.entity.article.Paragraph
import tech.lsfohtmbm.entity.article.ParagraphType

private const val PARAGRAPH_SEPARATOR = '|'
private const val PARAGRAPH_END = ']'
private const val PARAGRAPH_PRIMARY_HEADER = "[ph"
private const val PARAGRAPH_SECONDARY_HEADER = "[sh"
private const val PARAGRAPH_TEXT = "[t"
private const val PARAGRAPH_IMAGE = "[i"
private const val PARAGRAPH_DESCRIPTION = "[d"


internal class ParagraphMapper {
    fun mapToParagraphList(rawParagraphs: String): List<Paragraph> {
        val paragraphs = mutableListOf<Paragraph>()
        var prevSeparatorIndex = -1
        while (true) {
            val typeEndIndex = rawParagraphs.indexOf(PARAGRAPH_SEPARATOR, prevSeparatorIndex)
            if (typeEndIndex == -1) break
            val type = rawParagraphs.substring(prevSeparatorIndex + 1, typeEndIndex)
            val textEndIndex = rawParagraphs.indexOf(PARAGRAPH_END, typeEndIndex)
            val text = rawParagraphs.substring(typeEndIndex + 1, textEndIndex)
            val mappedType = when(type) {
                PARAGRAPH_PRIMARY_HEADER -> ParagraphType.PRIMARY_HEADER
                PARAGRAPH_SECONDARY_HEADER -> ParagraphType.SECONDARY_HEADER
                PARAGRAPH_TEXT -> ParagraphType.TEXT
                PARAGRAPH_IMAGE -> ParagraphType.IMAGE
                PARAGRAPH_DESCRIPTION -> ParagraphType.DESCRIPTION
                else -> null
            }

            if (mappedType != null) {
                paragraphs.add(Paragraph(mappedType, text))
            }

            prevSeparatorIndex = textEndIndex
        }

        return paragraphs
    }
}
