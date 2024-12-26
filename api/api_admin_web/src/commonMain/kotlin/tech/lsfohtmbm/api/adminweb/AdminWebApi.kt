package tech.lsfohtmbm.api.adminweb

object AdminWebApi {
    private const val SEGMENT_ARTICLES = "articles"

    const val ENDPOINT_ARTICLE = "$SEGMENT_ARTICLES/article"
    const val ENDPOINT_PREVIEWS = "$SEGMENT_ARTICLES/previews"
    const val ENDPOINT_ATTACHMENT = "attachment"

    const val PARAM_ID = "id"
}
