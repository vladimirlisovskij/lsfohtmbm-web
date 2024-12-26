package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class Previews(
    val previews: List<Preview>
)
