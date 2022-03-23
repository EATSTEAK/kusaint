package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfCardinality
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData

@kotlinx.serialization.Serializable
class MessageArea(override val id: String) : Component {
    fun reposition(top: String, left: String) = EventBuilder("MessageArea", "Reposition") {
        parameter("Id", id)
        parameter("Top", top)
        parameter("Left", left)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            enqueueCardinality = UcfCardinality.SINGLE
        }
    }
}