package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    val id: Long,
    val title: String,
    val date: DateWrapper,
    val ready: Boolean
)
