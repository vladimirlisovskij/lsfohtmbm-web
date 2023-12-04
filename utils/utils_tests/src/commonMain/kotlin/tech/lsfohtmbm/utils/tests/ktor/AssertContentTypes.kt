package tech.lsfohtmbm.utils.tests.ktor

import io.ktor.http.ContentType
import kotlin.test.assertTrue

fun assertContentTypes(
    expected: ContentType,
    actual: ContentType
) {
    assertTrue(actual.match(expected))
}
