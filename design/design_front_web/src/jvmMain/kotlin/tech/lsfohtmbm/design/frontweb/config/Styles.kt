package tech.lsfohtmbm.design.frontweb.config

import kotlinx.html.HEAD
import kotlinx.html.link

fun HEAD.styles() {
    link(rel = "stylesheet", href = "/static/styles.css")
}
