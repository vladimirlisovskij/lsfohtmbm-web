package tech.lsfohtmbm.page.admin

import kotlinx.browser.document
import react.*
import react.dom.client.createRoot

fun main() {
    val content = mainFlowFc.create()
    createRoot(document.body!!).render(content)
}
