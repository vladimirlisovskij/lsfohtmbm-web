package tech.lsfohtmbm.page.admin

import react.FC
import react.Props
import react.dom.html.ReactHTML.button

internal external interface MainProps : Props {
    var onCreateClicked: () -> Unit
    var onPreviewsClicked: () -> Unit
}

internal val mainFc = FC<MainProps> { props ->
    button {
        + "Создать"
        onClick = { props.onCreateClicked() }
    }

    button {
        + "Список"
        onClick = { props.onPreviewsClicked() }
    }
}
