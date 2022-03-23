package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData

@kotlinx.serialization.Serializable
class Custom(override val id: String) : Component {
    fun clientInfos(documentDomain: String, clientUrl: String) = EventBuilder("Custom", "ClientInfos") {
        parameter("Id", id)
        parameter("WindowOpenerExists", "false")
        parameter("ClientURL", clientUrl)
        parameter("ClientWidth", "1440")
        parameter("ClientHeight", "790")
        parameter("DocumentDomain", documentDomain)
        parameter("IsTopWindow", "true")
        parameter("ParentAccessible", "true")

        ucfParameter {
            action = UcfAction.ENQUEUE
            responseData = UcfResponseData.DELTA
        }
    }
}