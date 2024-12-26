package tech.lsfohtmbm.server.storage.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.mockk
import tech.lsfohtmbm.server.storage.configureEnvironment
import tech.lsfohtmbm.source.database.api.DataBaseSource

private const val HOST = "192.168.0.1"
private const val PORT = 8080
private const val BASE_URL = "http://$HOST:$PORT"

internal fun <T : Any> buildTestRunner(
    configurator: TestRunner.Dependencies.() -> T
): TestRunner<T> {
    val dependencies = TestRunner.Dependencies()
    val parameters = configurator(dependencies)
    return TestRunner(dependencies, parameters)
}

internal class TestRunner<T : Any>(
    private val dependencies: Dependencies,
    private val parameters: T
) {

    fun runTest(
        testBody: suspend Environment<T>.() -> Unit,
    ) = testApplication {
        setupTestEnvironment(dependencies.dataBaseSource)
        testBody(Environment(createHttpClient(), dependencies.dataBaseSource, parameters))
    }

    private fun ApplicationTestBuilder.createHttpClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) { json() }
            defaultRequest { url(BASE_URL) }
        }
    }

    private fun ApplicationTestBuilder.setupTestEnvironment(
        dataBaseSource: DataBaseSource,
    ) {
        environment {
            configureEnvironment(
                dataBaseSource,
                PORT,
                HOST
            )
        }
    }

    class Dependencies(
        val dataBaseSource: DataBaseSource = mockk()
    )

    class Environment<T : Any>(
        val httpClient: HttpClient,
        val dataBaseSource: DataBaseSource,
        val parameter: T
    )
}
