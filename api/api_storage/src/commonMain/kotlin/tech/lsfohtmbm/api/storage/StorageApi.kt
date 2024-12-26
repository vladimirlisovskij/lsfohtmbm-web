package tech.lsfohtmbm.api.storage

object StorageApi {
    private const val SEGMENT_ARTICLES = "articles"
    const val ENDPOINT_ARTICLES_PREVIEWS = "$SEGMENT_ARTICLES/list"
    const val ENDPOINT_ARTICLES_ARTICLE = "$SEGMENT_ARTICLES/article"

    private const val SEGMENT_ATTACHMENTS = "attachments"
    const val ENDPOINT_ATTACHMENTS_ATTACHMENT = "$SEGMENT_ATTACHMENTS/attachment"

    const val PARAM_ID = "id"
    const val PARAM_IS_FINISHED = "is_finished"
    const val PARAM_ARTICLE_ID = "article_id"
}
