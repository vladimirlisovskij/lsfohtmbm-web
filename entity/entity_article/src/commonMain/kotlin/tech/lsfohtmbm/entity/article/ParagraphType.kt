package tech.lsfohtmbm.entity.article

import kotlinx.serialization.Serializable

@Serializable
enum class ParagraphType {
    PRIMARY_HEADER,
    SECONDARY_HEADER,
    TEXT,
    IMAGE,
    DESCRIPTION
}