package tech.lsfohtmbm.source.admin.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf
import tech.lsfohtmbm.api.adminweb.AdminWebApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.admin.api.AdminSource

class KtorAdminSource(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : AdminSource {
    override suspend fun getPreviews(): Previews? {
        return runCatching {
            httpClient
                .get("$baseUrl/${AdminWebApi.ENDPOINT_PREVIEWS}")
                .body<Previews>()
        }.getOrNull()
    }

    override suspend fun getArticle(id: Long): Article? {
        return runCatching {
            httpClient
                .get("$baseUrl/${AdminWebApi.ENDPOINT_ARTICLE}") {
                    parameter(AdminWebApi.PARAM_ID, id.toString())
                }.body<Article>()
        }.getOrNull()
    }

    override suspend fun delete(id: Long): Boolean {
        return runCatching {
            val response = httpClient.post("$baseUrl/${AdminWebApi.ENDPOINT_DELETE}") {
                setBody(
                    FormDataContent(
                        parametersOf(AdminWebApi.PARAM_ID, id.toString())
                    )
                )
            }

            response.status.isSuccess()
        }.getOrDefault(false)
    }

    override suspend fun insert(article: Article): InsertionResult? {
        return runCatching {
            httpClient.post("$baseUrl/${AdminWebApi.ENDPOINT_INSERT}") {
                setBody(article)
                contentType(ContentType.Application.Json)
            }.body<InsertionResult>()
        }.getOrNull()
    }
}
