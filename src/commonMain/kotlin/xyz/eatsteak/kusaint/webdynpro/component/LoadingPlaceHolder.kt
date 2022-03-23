package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData

@kotlinx.serialization.Serializable
class LoadingPlaceHolder(override val id: String) : Component {

    fun load() = EventBuilder("LoadingPlaceHolder", "Load") {
        parameter("Id", id)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            action = UcfAction.SUBMIT
        }
    }
}