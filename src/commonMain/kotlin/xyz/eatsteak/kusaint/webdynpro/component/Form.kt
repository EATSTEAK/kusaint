package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData

@kotlinx.serialization.Serializable
class Form(override val id: String) : Component {

    fun request() = EventBuilder("Form", "Request") {
        parameter("Id", id)
        parameter("Async", "false")
        parameter("Hash", "")
        parameter("DomChanged", "false")
        parameter("IsDirty", "false")

        ucfParameter {
            responseData = UcfResponseData.DELTA
        }
    }
}