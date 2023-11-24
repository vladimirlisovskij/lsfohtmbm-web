package tech.lsfohtmbm.page.admin

import kotlinx.coroutines.launch
import react.*
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.Paragraph
import tech.lsfohtmbm.entity.storage.ParagraphType
import kotlin.js.Date

internal external interface EditorProps : Props {
    var id: Long?
    var onBackClicked: () -> Unit
}

internal val editorFc = FC<EditorProps> { props ->
    var state by useState<EditorState>(EditorState.Loading)

    useEffectOnce {
        val curId = props.id
        if (curId != null) {
            mainScope.launch {
                state = EditorState.Loading
                val article = source.getArticle(curId)
                state = if (article != null) {
                    EditorState.Editor(
                        article = article.toUiModel(),
                        isRequestError = false,
                        isValidationError = false
                    )
                } else {
                    EditorState.Loading
                }
            }
        } else {
            state = EditorState.Editor(
                article = emptyArticleModel(),
                isRequestError = false,
                isValidationError = false
            )
        }
    }

    button {
        + "Назад"
        onClick = { props.onBackClicked() }
    }

    when (val curState = state) {
        is EditorState.Editor -> {
            editor(
                article = curState.article,
                isRequestError = curState.isRequestError,
                isValidationError = curState.isValidationError,
                onArticleChanged = { state = curState.copy(article = it) },
                onSaveClicked = {
                    mainScope.launch {
                        val domainModel = it.toDomainModel()
                        if (domainModel != null) {
                            state = EditorState.Loading
                            val result = source.insert(domainModel)
                            state = if (result != null) {
                                val newArticleId = result.newArticleId
                                EditorState.Editor(
                                    article = it.copy(id = newArticleId),
                                    isRequestError = false,
                                    isValidationError = false
                                )
                            } else {
                                EditorState.Editor(
                                    article = it,
                                    isRequestError = true,
                                    isValidationError = false
                                )
                            }
                        } else {
                            EditorState.Editor(
                                article = it,
                                isRequestError = false,
                                isValidationError = true
                            )
                        }
                    }
                }
            )
        }

        EditorState.Error -> {
            error()
        }

        EditorState.Loading -> {
            loading()
        }
    }
}

private fun Article.toUiModel(): ArticleModel {
    return ArticleModel(
        id = id,
        date = Date(date.year, date.month, date.day).toUiValue(),
        title = title,
        fields = paragraphs.map { ParagraphModel(it.type.name, it.value) }
    )
}

private fun emptyArticleModel(): ArticleModel {
    return ArticleModel(
        null,
        currentDate(),
        "",
        emptyList()
    )
}

private fun currentDate(): String {
    return Date().toUiValue()
}

private fun Date.toUiValue(): String {
    return toISOString().slice(0..9)
}

private fun ArticleModel.toDomainModel(): Article? {
    val date = date.split("-").mapNotNull { it.toIntOrNull() }
    if (date.size != 3) {
        return null
    }

    val dateWrapper = DateWrapper(date[2], date[1], date[0])
    val paragraph = fields
        .mapNotNull { paragraph ->
            ParagraphType.entries
                .firstOrNull { it.name == paragraph.type }
                ?.let { Paragraph(it, paragraph.value) }
        }

    return Article(
        id = id ?: -1,
        title = title,
        date = dateWrapper,
        paragraphs = paragraph
    )
}


private fun ChildrenBuilder.error() {
    ReactHTML.p {
        +"Ошибка"
    }
}

private fun ChildrenBuilder.loading() {
    ReactHTML.p {
        + "Загрузка..."
    }
}

private fun ChildrenBuilder.editor(
    article: ArticleModel,
    isRequestError: Boolean,
    isValidationError: Boolean,
    onArticleChanged: (ArticleModel) -> Unit,
    onSaveClicked: (ArticleModel) -> Unit
) {
    div {
        input {
            value = article.date
            type = InputType.date
            onChange = {
                println("change")
                println(it.target.valueAsDate as Date)
                val newValue = (it.target.valueAsDate as? Date)?.toUiValue()
                if (newValue != null) {
                    onArticleChanged(article.copy(date = newValue))
                }
            }
        }
    }

    div {
        input {
            value = article.title
            onChange = {
                onArticleChanged(article.copy(title = it.target.value))
            }
        }
    }

    div {
        button {
            + "добавить"
            onClick = {
                val newFields = article.fields
                    .toMutableList()
                    .apply { add(ParagraphModel(ParagraphType.TEXT.name, "")) }

                onArticleChanged(article.copy(fields = newFields))
            }
        }
    }

    article.fields.forEachIndexed { index, model ->
        div {
            select {
                value = model.type
                ParagraphType.entries.forEach {
                    option {
                        value = it.name
                        + it.name
                    }
                }

                onChange = {
                    val newFields = article.fields
                        .toMutableList()
                        .apply { set(index, model.copy(type = it.target.value)) }

                    onArticleChanged(article.copy(fields = newFields))
                }
            }

            input {
                value = model.value
                type = InputType.text
                onChange = {
                    val newFields = article.fields
                        .toMutableList()
                        .apply { set(index, model.copy(value = it.target.value)) }

                    onArticleChanged(article.copy(fields = newFields))
                }
            }

            button {
                + "Добавить"
                onClick = {
                    val newFields = article.fields
                        .toMutableList()
                        .apply { add(index, ParagraphModel(ParagraphType.TEXT.name, "")) }

                    onArticleChanged(article.copy(fields = newFields))
                }
            }

            button {
                + "Удалить"
                onClick = {
                    val newFields = article.fields
                        .toMutableList()
                        .apply { removeAt(index) }

                    onArticleChanged(article.copy(fields = newFields))
                }
            }
        }
    }

    div {
        button {
            + "Сохранить"
            onClick = { onSaveClicked(article) }
        }

        if (isRequestError) {
            + "Ошибка запроса"
        }

        if (isValidationError) {
            + "Ошибка валидации"
        }
    }
}

private sealed interface EditorState {
    data object Loading : EditorState

    data object Error : EditorState

    data class Editor(
        val article: ArticleModel,
        val isRequestError: Boolean,
        val isValidationError: Boolean
    ) : EditorState
}

private data class ArticleModel(
    val id: Long?,
    val date: String,
    val title: String,
    val fields: List<ParagraphModel>
)

private data class ParagraphModel(
    val type: String,
    val value: String
)
