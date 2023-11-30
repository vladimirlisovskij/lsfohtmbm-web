package tech.lsfohtmbm.page.admin

import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.script
import kotlinx.html.title

fun HTML.adminEditorPage() {
    head {
        title("admin")
    }

    body {
        script(src = "/static/page_admin.js") { }
    }
}
