package tech.lsfohtmbm.server.frontweb.modules

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import tech.lsfohtmbm.server.frontweb.PageConfig
import tech.lsfohtmbm.server.frontweb.modules.routing.configureArticlePage
import tech.lsfohtmbm.server.frontweb.modules.routing.configureAttachments
import tech.lsfohtmbm.server.frontweb.modules.routing.configurePreviewsPage
import tech.lsfohtmbm.server.frontweb.modules.routing.configureRootPage
import tech.lsfohtmbm.server.frontweb.modules.routing.configureStaticResources
import tech.lsfohtmbm.source.storage.api.StorageSource

internal fun Application.configureRouting(
    storageSource: StorageSource,
    pageConfig: PageConfig
) {
    routing {
        configureRootPage(pageConfig.mainPage)
        configureArticlePage(storageSource, pageConfig.articleRenderer)
        configurePreviewsPage(storageSource, pageConfig.articlesListPage)
        configureAttachments(storageSource)
        configureStaticResources()
    }
}
