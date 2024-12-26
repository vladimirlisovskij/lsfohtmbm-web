package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class FinishedPreviews(
    val previews: List<FinishedPreview>
)
