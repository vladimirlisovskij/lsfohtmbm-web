package tech.lsfohtmbm.source.database.impl.mapper

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import tech.lsfohtmbm.entity.article.DateWrapper

private const val DAY = 11
private const val MONTH = 5
private const val YEAR = 2023
private const val RAW_DATE = (DAY or (MONTH shl 5) or (YEAR shl 9)).toLong()

class DateMapperTest {

    private val mapper = DateMapper()

    @Test
    fun mapToDateWrapper() {
        val mappedDate = mapper.mapToDateWrapper(RAW_DATE)
        assertEquals(DAY, mappedDate.day)
        assertEquals(MONTH, mappedDate.month)
        assertEquals(YEAR, mappedDate.year)
    }

    @Test
    fun mapToLong() {
        val rawDate = DateWrapper(DAY, MONTH, YEAR)
        val mappedDate = mapper.mapToLong(rawDate)
        assertEquals(RAW_DATE, mappedDate)
    }
}