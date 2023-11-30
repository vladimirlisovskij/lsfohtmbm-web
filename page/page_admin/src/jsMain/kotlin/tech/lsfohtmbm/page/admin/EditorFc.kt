package tech.lsfohtmbm.page.admin

import kotlinx.coroutines.launch
import react.ChildrenBuilder
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useEffectOnce
import react.useState
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.Paragraph
import tech.lsfohtmbm.entity.storage.ParagraphType
import web.html.InputType
import kotlin.js.Date

internal val EditorFc = FC<EditorProps> { props ->
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
        +"Назад"
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

internal external interface EditorProps : Props {
    var id: Long?
    var onBackClicked: () -> Unit
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

@Suppress("MagicNumber")
private fun Date.toUiValue(): String {
    return toISOString()
        .slice(0..9)
}

private const val DATE_SEGMENTS_COUNT = 3
private const val SEGMENT_DAY = 2
private const val SEGMENT_MONTH = 1
private const val SEGMENT_YEAR = 2

private fun ArticleModel.toDomainModel(): Article? {
    val date = date.split("-").mapNotNull { it.toIntOrNull() }
    if (date.size != DATE_SEGMENTS_COUNT) {
        return null
    }

    val dateWrapper = DateWrapper(
        date[SEGMENT_DAY],
        date[SEGMENT_MONTH],
        date[SEGMENT_YEAR]
    )

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
        +"Загрузка..."
    }
}

private fun ChildrenBuilder.editor(
    article: ArticleModel,
    isRequestError: Boolean,
    isValidationError: Boolean,
    onArticleChanged: (ArticleModel) -> Unit,
    onSaveClicked: (ArticleModel) -> Unit
) {
    dateInput(
        date = article.date,
        onDateChanged = { newDate ->
            if (newDate != null) {
                onArticleChanged(article.copy(date = newDate))
            }
        }
    )

    titleInput(
        title = article.title,
        onTitleChanged = { onArticleChanged(article.copy(title = it)) }
    )

    addRowButton {
        val newFields = article.fields
            .toMutableList()
            .apply { add(ParagraphModel(ParagraphType.TEXT.name, "")) }

        onArticleChanged(article.copy(fields = newFields))
    }

    article.fields.forEachIndexed { index, model ->
        articleRow(
            paragraphType = model.type,
            paragraphValue = model.value,
            onTypeChanged = {
                val newFields = article.fields
                    .toMutableList()
                    .apply { set(index, model.copy(type = it)) }

                onArticleChanged(article.copy(fields = newFields))
            },
            onValueChanged = {
                val newFields = article.fields
                    .toMutableList()
                    .apply { set(index, model.copy(value = it)) }

                onArticleChanged(article.copy(fields = newFields))
            },
            onAddRowAboveClicked = {
                val newFields = article.fields
                    .toMutableList()
                    .apply { add(index, ParagraphModel(ParagraphType.TEXT.name, "")) }

                onArticleChanged(article.copy(fields = newFields))
            },
            onDeleteClicked = {
                val newFields = article.fields
                    .toMutableList()
                    .apply { removeAt(index) }

                onArticleChanged(article.copy(fields = newFields))
            }
        )
    }

    saveButton(
        isValidationError = isValidationError,
        isRequestError = isRequestError,
        onSaveClicked = { onSaveClicked(article) }
    )
}

private fun ChildrenBuilder.dateInput(
    date: String,
    onDateChanged: (String?) -> Unit
) {
    div {
        input {
            value = date
            type = InputType.date
            onChange = { onDateChanged(it.target.valueAsDate?.toUiValue()) }
        }
    }
}

private fun ChildrenBuilder.titleInput(
    title: String,
    onTitleChanged: (String) -> Unit
) {
    div {
        input {
            value = title
            onChange = { onTitleChanged(it.target.value) }
        }
    }
}

private fun ChildrenBuilder.addRowButton(
    onClick: () -> Unit
) {
    div {
        button {
            +"Добавить"
            this.onClick = { onClick() }
        }
    }
}

private fun ChildrenBuilder.articleRow(
    paragraphType: String,
    paragraphValue: String,
    onTypeChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit,
    onAddRowAboveClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    div {
        select {
            value = paragraphType
            ParagraphType.entries.forEach {
                option {
                    value = it.name
                    +it.name
                }
            }

            onChange = { onTypeChanged(it.target.value) }
        }

        input {
            value = paragraphValue
            type = InputType.text
            onChange = { onValueChanged(it.target.value) }
        }

        button {
            +"Добавить"
            onClick = { onAddRowAboveClicked() }
        }

        button {
            +"Удалить"
            onClick = { onDeleteClicked() }
        }
    }
}

private fun ChildrenBuilder.saveButton(
    isValidationError: Boolean,
    isRequestError: Boolean,
    onSaveClicked: () -> Unit
) {
    div {
        button {
            +"Сохранить"
            onClick = { onSaveClicked() }
        }

        if (isRequestError) {
            +"Ошибка запроса"
        }

        if (isValidationError) {
            +"Ошибка валидации"
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
