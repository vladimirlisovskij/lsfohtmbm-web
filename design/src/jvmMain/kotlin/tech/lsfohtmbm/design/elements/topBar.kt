package tech.lsfohtmbm.design.elements

import kotlinx.html.*

fun FlowContent.topBarSegment(ref: String, name: String) {
    a(href = ref) { + name }
}

fun FlowContent.topBar() {
    header {
        p {
            + "long stairs from obsolete human to meat based machine"
        }

        nav {
            topBarSegment("/", "Главная")
            topBarSegment("/articles", "Блог")
            topBarSegment("/blog", "GitHub")
        }
    }
}
