package tech.lsfohtmbm.entity.article

import kotlinx.serialization.Serializable

@Serializable
class Paragraph(
    val type: ParagraphType,
    val value: String
)