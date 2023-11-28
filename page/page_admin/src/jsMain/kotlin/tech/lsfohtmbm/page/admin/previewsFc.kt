package tech.lsfohtmbm.page.admin

import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import tech.lsfohtmbm.entity.storage.ArticlePreview
import web.cssom.ClassName

internal external interface PreviewsProps : Props {
    var onBackClicked: () -> Unit
    var onArticleClicked: (Long) -> Unit
}

internal val previewsFc = FC<PreviewsProps> { props ->
    var previewState by useState<PreviewState>(PreviewState.Loading)

    useEffectOnce {
        mainScope.launch {
            previewState = PreviewState.Loading
            val previews = source.getPreviews()
            previewState = if (previews != null) {
                PreviewState.Complete(previews.previews)
            } else {
                PreviewState.Error
            }
        }
    }

    button {
        + "Назад"

        onClick = { props.onBackClicked() }
    }

    when (val curState = previewState) {
        is PreviewState.Complete -> {
            curState.previews.forEach {
                preview(
                    preview = it,
                    onArticleClicked = props.onArticleClicked,
                    onArticleRemoved = { id ->
                        mainScope.launch {
                            previewState = PreviewState.Loading
                            previewState = if (source.delete(id)) {
                                val previews = source.getPreviews()
                                if (previews != null) {
                                    PreviewState.Complete(previews.previews)
                                } else {
                                    PreviewState.Error
                                }
                            } else {
                                PreviewState.Error
                            }
                        }
                    }
                )
            }
        }

        PreviewState.Error -> {
            error()
        }

        PreviewState.Loading -> {
            loading()
        }
    }
}

private fun ChildrenBuilder.preview(
    preview: ArticlePreview,
    onArticleClicked: (Long) -> Unit,
    onArticleRemoved: (Long) -> Unit
) {
    div {
        className = ClassName("preview")

        + "${preview.date.day}/${preview.date.month}/${preview.date.year} ${preview.title}"

        button {
            + "изменить"
            onClick = { onArticleClicked(preview.id)  }
        }

        button {
            + "удалить"
            onClick = { onArticleRemoved(preview.id)  }
        }
    }
}

private fun ChildrenBuilder.error() {
    p {
        + "Ошибка"
    }
}

private fun ChildrenBuilder.loading() {
    p {
        + "Загрузка..."
    }
}

private sealed interface PreviewState {
    data object Loading : PreviewState
    data object Error : PreviewState

    data class Complete(val previews: List<ArticlePreview>) : PreviewState
}