package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.app.WebDynProApplication
import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData
import kotlinx.serialization.Serializable

@Serializable
class Button(override val id: String): Component {
    fun press() = EventBuilder("Button", "Press") {
        parameter("Id", id)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            action = UcfAction.SUBMIT
        }
    }
}

fun WebDynProApplication.pressButton(button: Button) = actionWithNotify {
    add(button.press())
}