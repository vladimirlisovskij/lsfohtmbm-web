package tech.lsfohtmbm.page.admin

import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import tech.lsfohtmbm.source.admin.impl.createAdminSource

internal val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
internal val source = createAdminSource(window.location.origin)
