package tech.lsfohtmbm.entity.article

import kotlinx.serialization.Serializable

@Serializable
data class ArticlePreview(
    val id: Long,
    val title: String,
    val date: DateWrapper
)