package tech.lsfohtmbm.databasesource.impl.mapper

import tech.lsfohtmbm.article.DateWrapper


private const val SHIFT_DAY = 0
private const val SHIFT_MONTH = SHIFT_DAY + 5
private const val SHIFT_YEAR = SHIFT_MONTH + 4
private const val MASK_DAY = 31
private const val MASK_MONTH = 15
private const val MASK_YEAR = 4095

internal class DateMapper {
    fun mapToDateWrapper(date: Long): DateWrapper {
        return with(date.toInt()) {
            DateWrapper(
                day = getMaskedValue(MASK_DAY, SHIFT_DAY),
                month = getMaskedValue(MASK_MONTH, SHIFT_MONTH),
                year = getMaskedValue(MASK_YEAR, SHIFT_YEAR)
            )
        }
    }

    fun mapToLong(date: DateWrapper): Long {
        return 0
            .addMaskedValue(SHIFT_DAY, date.day)
            .addMaskedValue(SHIFT_MONTH, date.month)
            .addMaskedValue(SHIFT_YEAR, date.year)
            .toLong()
    }

    private fun Int.getMaskedValue(mask: Int, shift: Int): Int {
        return (this shr shift) and mask
    }

    private fun Int.addMaskedValue(shift: Int, value: Int): Int {
        return this or (value shl shift)
    }
}