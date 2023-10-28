package tech.lsfohtmbm.design.config

import kotlinx.html.HEAD
import kotlinx.html.title

fun HEAD.screenTitle(title: String) {
    title("lsfohtmbm • $title")
}