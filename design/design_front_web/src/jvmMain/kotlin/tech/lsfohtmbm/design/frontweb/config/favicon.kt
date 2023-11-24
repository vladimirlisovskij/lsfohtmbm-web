package tech.lsfohtmbm.design.frontweb.config

import kotlinx.html.HEAD
import kotlinx.html.link

fun HEAD.favicon() {
    link {
        rel = "apple-touch-icon"
        sizes = "180x180"
        href = "/static/apple-touch-icon.png"
    }

    link {
        rel = "icon"
        sizes = "32x32"
        type = "image/png"
        href = "/static/favicon-32x32.png"
    }

    link {
        rel = "icon"
        sizes = "16x16"
        type = "image/png"
        href = "/static/favicon-16x16.png"
    }

    link {
        rel = "manifest"
        href = "/static/site.webmanifest"
    }
}