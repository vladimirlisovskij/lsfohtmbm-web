package tech.lsfohtmbm.design.frontweb.config

import kotlinx.html.HEAD
import kotlinx.html.title

fun HEAD.screenTitle(title: String) {
    title("lsfohtmbm • $title")
}