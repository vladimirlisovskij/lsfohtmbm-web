package tech.lsfohtmbm.page.admin

import js.core.jso
import react.FC
import react.Props
import react.useState

internal val mainFlowFc = FC<Props> {
    var mainState by useState<MainState>(MainState.Main)

    when (val curState = mainState) {
        is MainState.Editor -> {
            child(
                EditorFc,
                jso {
                    id = curState.id
                    onBackClicked = { mainState = MainState.Main }
                }
            )
        }

        MainState.Main -> {
            child(
                MainFc,
                jso {
                    onCreateClicked = { mainState = MainState.Editor(null) }
                    onPreviewsClicked = { mainState = MainState.Previews }
                }
            )
        }

        MainState.Previews -> {
            child(
                PreviewsFc,
                jso {
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
