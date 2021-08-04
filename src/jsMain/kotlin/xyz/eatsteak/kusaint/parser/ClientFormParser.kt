package xyz.eatsteak.kusaint.parser

import io.ktor.http.*
import org.w3c.dom.asList
import org.w3c.dom.parsing.DOMParser
import xyz.eatsteak.kusaint.eventqueue.model.SapClient
import xyz.eatsteak.kusaint.state.State

actual object ClientFormParser: Parser<String, SapClient> {

    actual override suspend fun parse(state: State<String>): SapClient {
        val keys = mutableMapOf<String, String>()
        state.mutations.forEach { actionRes ->
            if(actionRes.response.contentType()?.equals(ContentType("text", "html")) == true) {
                println(actionRes.response.status)
                val doc = DOMParser().parseFromString(actionRes.result, actionRes.response.contentType()!!.toString())
                val clientElem = doc.querySelector("#sap\\.client\\.SsrClient\\.form")
                if(clientElem != null) {
                    keys["action"] = clientElem.getAttribute("action")!!
                    clientElem.children.asList().forEach {
                        keys[it.id] = it.getAttribute("value")!!
                    }
                }
            }
        }
        return SapClient(
            action = keys["action"]!!,
            charset = keys["sap-charset"]!!,
            wdSecureId = keys["sap-wd-secure-id"]!!,
            appName = keys["fesrAppName"]!!,
            useBeacon = (keys["fesrUseBeacon"] == "true")
        )
    }
}