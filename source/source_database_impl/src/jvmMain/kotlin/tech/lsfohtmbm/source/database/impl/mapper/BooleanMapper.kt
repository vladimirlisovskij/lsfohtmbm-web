package tech.lsfohtmbm.source.database.impl.mapper

private const val LONG_TRUE = 1L
private const val LONG_FALSE = 0L

internal class BooleanMapper {
    fun mapToLong(value: Boolean): Long {
        return if (value) LONG_TRUE else LONG_FALSE
    }

    fun mapToBoolean(value: Long): Boolean {
        return value == LONG_TRUE
    }
}
