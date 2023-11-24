package tech.lsfohtmbm.page.admin

import kotlinx.html.*

fun HTML.adminEditorPage() {
    head {
        title("admin")
    }

    body {
        script(src = "/static/page_admin.js") { }
    }
}
