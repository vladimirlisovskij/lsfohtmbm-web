package tech.lsfohtmbm.source.database.impl.mapper

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import tech.lsfohtmbm.entity.storage.ParagraphType

private const val PRIMARY_HEADER = "PRIMARY_HEADER"
private const val SECONDARY_HEADER = "SECONDARY_HEADER"
private const val TEXT = "TEXT"
private const val DESCRIPTION = "DESCRIPTION"

class ParagraphMapperTest {

    private val mapper = ParagraphMapper()

    @Test
    fun mapToParagraphList() {

        val rawParagraphs = "[ph|$PRIMARY_HEADER][sh|$SECONDARY_HEADER][t|$TEXT][d|$DESCRIPTION]"
        val mappedParagraphs = mapper.mapToParagraphList(rawParagraphs)
        assertEquals(4, mappedParagraphs.size)

        with(mappedParagraphs[0]) {
            assertEquals(type, ParagraphType.PRIMARY_HEADER)
            assertEquals(value, PRIMARY_HEADER)
        }

        with(mappedParagraphs[1]) {
            assertEquals(type, ParagraphType.SECONDARY_HEADER)
            assertEquals(value, SECONDARY_HEADER)
        }

        with(mappedParagraphs[2]) {
            assertEquals(type, ParagraphType.TEXT)
            assertEquals(value, TEXT)
        }

        with(mappedParagraphs[3]) {
            assertEquals(type, ParagraphType.DESCRIPTION)
            assertEquals(value, DESCRIPTION)
        }
    }
}