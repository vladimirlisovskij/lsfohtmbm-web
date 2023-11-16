package tech.lsfohtmbm.entity.article

import kotlinx.serialization.Serializable

@Serializable
data class DateWrapper(
    val day: Int,
    val month: Int,
    val year: Int
)