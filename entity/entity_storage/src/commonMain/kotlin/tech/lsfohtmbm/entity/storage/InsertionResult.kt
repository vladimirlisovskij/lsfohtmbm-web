package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class InsertionResult(
    val newArticleId: Long
)
