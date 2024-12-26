package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class FinishedPreview(
    val id: Long,
    val title: String,
    val date: DateWrapper
)
