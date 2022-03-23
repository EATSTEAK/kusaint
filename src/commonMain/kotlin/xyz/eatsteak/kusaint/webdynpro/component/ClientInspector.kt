package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfCardinality
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData

@kotlinx.serialization.Serializable
class ClientInspector(override val id: String): Component {
    fun notify(data: String) = EventBuilder("ClientInspector", "Notify") {
        parameter("Id", id)
        parameter("Data", data)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            enqueueCardinality = UcfCardinality.SINGLE
        }
    }
}