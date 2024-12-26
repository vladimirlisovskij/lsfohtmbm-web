package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.streams.asInput
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.Attachment
import tech.lsfohtmbm.entity.storage.FinishedArticle
import tech.lsfohtmbm.entity.storage.FinishedPreviews
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.storage.api.StorageSource
import tech.lsfohtmbm.utils.coroutines.cancellableGetOrDefault
import tech.lsfohtmbm.utils.coroutines.cancellableGetOrNull
import java.io.ByteArrayInputStream

internal class KtorStorageSource(
    private val client: HttpClient,
    private val baseUrl: String
) : StorageSource {

    override suspend fun getFinishedPreviews(): FinishedPreviews? {
        return runCatching {
            client
                .get("$baseUrl/${StorageApi.ENDPOINT_ARTICLES_PREVIEWS}") {
                    parameter(StorageApi.PARAM_IS_FINISHED, true)
                }.body<FinishedPreviews>()
        }.cancellableGetOrNull()
    }

    override suspend fun getPreviews(): Previews? {
        return runCatching {
            client
                .get("$baseUrl/${StorageApi.ENDPOINT_ARTICLES_PREVIEWS}") {
                    parameter(StorageApi.PARAM_IS_FINISHED, false)
                }.body<Previews>()
        }.cancellableGetOrNull()
    }

    override suspend fun getFinishedArticle(articleId: Long): FinishedArticle? {
        return runCatching {
            client
                .get("$baseUrl/${StorageApi.ENDPOINT_ARTICLES_ARTICLE}") {
                    parameter(StorageApi.PARAM_IS_FINISHED, true)
                    parameter(StorageApi.PARAM_ID, articleId)
                }.body<FinishedArticle>()
        }.cancellableGetOrNull()
    }

    override suspend fun getArticle(articleId: Long): Article? {
        return runCatching {
            client
                .get("$baseUrl/${StorageApi.ENDPOINT_ARTICLES_ARTICLE}") {
                    parameter(StorageApi.PARAM_IS_FINISHED, false)
                    parameter(StorageApi.PARAM_ID, articleId)
                }.body<Article>()
        }.cancellableGetOrNull()
    }

    override suspend fun deleteArticle(articleId: Long): Boolean {
        return runCatching {
            client
                .delete("$baseUrl/${StorageApi.ENDPOINT_ARTICLES_ARTICLE}") {
                    setBody(
                        FormDataContent(
                            parametersOf(StorageApi.PARAM_ID, articleId.toString())
                        )
                    )
                }.status.isSuccess()
        }.cancellableGetOrDefault(false)
    }

    override suspend fun insertArticle(article: Article): InsertionResult? {
        return runCatching {
            client.put("$baseUrl/${StorageApi.ENDPOINT_ARTICLES_ARTICLE}") {
                setBody(article)
                contentType(ContentType.Application.Json)
            }.body<InsertionResult>()
        }.cancellableGetOrNull()
    }

    override suspend fun getAttachment(attachmentId: Long, forFinishedArticle: Boolean): Attachment? {
        return runCatching {
            val response = client.get("$baseUrl/${StorageApi.ENDPOINT_ATTACHMENTS_ATTACHMENT}") {
                parameter(StorageApi.PARAM_ID, attachmentId)
                parameter(StorageApi.PARAM_IS_FINISHED, forFinishedArticle)
            }

            Attachment(
                data = response.readBytes(),
                type = response.headers.get(HttpHeaders.ContentType)!!
            )
        }.cancellableGetOrNull()
    }

    override suspend fun insertAttachment(articleId: Long, attachment: Attachment): InsertionResult? {
        return runCatching {
            client.put("$baseUrl/${StorageApi.ENDPOINT_ATTACHMENTS_ATTACHMENT}") {
                MultiPartFormDataContent(
                    parts = listOf(
                        PartData.FormItem(
                            value = articleId.toString(),
                            dispose = { },
                            partHeaders = Headers.build {
                                append(ContentDisposition.Parameters.Name, StorageApi.PARAM_ARTICLE_ID)
                            }
                        ),
                        PartData.BinaryItem(
                            provider = { ByteArrayInputStream(attachment.data).asInput() },
                            dispose = { },
                            partHeaders = Headers.Empty
                        )
                    )
                )
            }.body<InsertionResult>()
        }.cancellableGetOrNull()
    }

    override suspend fun updateAttachment(id: Long, attachment: Attachment): Boolean {
        return runCatching {
            client.patch("$baseUrl/${StorageApi.ENDPOINT_ATTACHMENTS_ATTACHMENT}") {
                MultiPartFormDataContent(
                    parts = listOf(
                        PartData.FormItem(
                            value = id.toString(),
                            dispose = { },
                            partHeaders = Headers.build {
                                append(ContentDisposition.Parameters.Name, StorageApi.PARAM_ID)
                            }
                        ),
                        PartData.BinaryItem(
                            provider = { ByteArrayInputStream(attachment.data).asInput() },
                            dispose = { },
                            partHeaders = Headers.Empty
                        )
                    )
                )
            }.status.isSuccess()
        }.cancellableGetOrDefault(false)
    }

    override suspend fun deleteAttachment(id: Long): Boolean {
        return runCatching {
            client.delete("$baseUrl/${StorageApi.ENDPOINT_ATTACHMENTS_ATTACHMENT}") {
                setBody(
                    FormDataContent(
                        parametersOf(StorageApi.PARAM_ID, id.toString())
                    )
                )
            }.status.isSuccess()
        }.cancellableGetOrDefault(false)
    }
}
