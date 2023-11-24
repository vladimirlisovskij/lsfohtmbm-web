package tech.lsfohtmbm.entity.storage

import kotlinx.serialization.Serializable

@Serializable
data class Previews(
    val previews: List<ArticlePreview>
)

@Serializable
data class ArticlePreview(
    val id: Long,
    val title: String,
    val date: DateWrapper
)
