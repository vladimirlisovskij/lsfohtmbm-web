package tech.lsfohtmbm.page.admin

import react.create
import react.dom.client.createRoot
import web.dom.document

fun main() {
    val content = mainFlowFc.create()
    createRoot(document.body).render(content)
}
