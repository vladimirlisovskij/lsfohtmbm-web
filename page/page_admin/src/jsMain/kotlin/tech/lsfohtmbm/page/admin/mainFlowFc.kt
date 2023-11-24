package tech.lsfohtmbm.page.admin

import react.*

internal val mainFlowFc = FC<Props> {
    var mainState by useState<MainState>(MainState.Main)

    when (val curState = mainState) {
        is MainState.Editor -> {
            child(
                editorFc.create {
                    id = curState.id
                    onBackClicked = { mainState = MainState.Main }
                }
            )
        }

        MainState.Main -> {
            child(
                mainFc.create {
                    onCreateClicked = { mainState = MainState.Editor(null) }
                    onPreviewsClicked = { mainState = MainState.Previews }
                }
            )
        }

        MainState.Previews -> {
            child(
                previewsFc.create {
                    onBackClicked = { mainState = MainState.Main }
                    onArticleClicked = { mainState = MainState.Editor(it) }
                }
            )
        }
    }
}

private sealed interface MainState {
    data object Main : MainState
    data object Previews : MainState

    data class Editor(val id: Long?) : MainState
}