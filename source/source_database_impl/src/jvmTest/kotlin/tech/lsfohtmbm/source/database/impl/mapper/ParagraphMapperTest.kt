package tech.lsfohtmbm.source.database.impl.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.entity.storage.Paragraph
import tech.lsfohtmbm.entity.storage.ParagraphType
import kotlin.test.assertContentEquals

private const val PRIMARY_HEADER = "PRIMARY_HEADER"
private const val SECONDARY_HEADER = "SECONDARY_HEADER"
private const val TEXT = "TEXT"
private const val DESCRIPTION = "DESCRIPTION"

class ParagraphMapperTest {

    private val mapper = ParagraphMapper()

    private val paragraphAssertionUnits = listOf(
        ParagraphAssertionUnit(
            Paragraph(ParagraphType.PRIMARY_HEADER, PRIMARY_HEADER),
            "[ph|$PRIMARY_HEADER]"
        ),
        ParagraphAssertionUnit(
            Paragraph(ParagraphType.SECONDARY_HEADER, SECONDARY_HEADER),
            "[sh|$SECONDARY_HEADER]"
        ),
        ParagraphAssertionUnit(
            Paragraph(ParagraphType.TEXT, TEXT),
            "[t|$TEXT]"
        ),
        ParagraphAssertionUnit(
            Paragraph(ParagraphType.DESCRIPTION, DESCRIPTION),
            "[d|$DESCRIPTION]"
        ),
    )

    private val paragraphList = paragraphAssertionUnits
        .map { it.paragraph }

    private val stringEntry = paragraphAssertionUnits
        .joinToString(separator = "") { it.formattedEntry }

    @Test
    fun `map String entry to Paragraph`() {
        val mappedParagraphs = mapper.mapToParagraphList(stringEntry)
        assertContentEquals(paragraphList, mappedParagraphs)
    }

    @Test
    fun `map Paragraph to String entry`() {
        val mappedStringEntry = paragraphAssertionUnits
            .map { it.paragraph }
            .let { mapper.mapToString(it) }

        val expectedStringEntry = paragraphAssertionUnits
            .joinToString(separator = "") { it.formattedEntry }

        assertEquals(expectedStringEntry, mappedStringEntry)
    }

    private class ParagraphAssertionUnit(
        val paragraph: Paragraph,
        val formattedEntry: String
    )
}
