package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class Paragraph(
    val type: ParagraphType,
    val value: String
)
