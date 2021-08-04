package xyz.eatsteak.kusaint.parser

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.w3c.dom.asList
import org.w3c.dom.parsing.DOMParser
import xyz.eatsteak.kusaint.eventqueue.model.SapClientData
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.decompressBrotli

actual object ClientFormParser: Parser<String, SapClientData> {

    actual override suspend fun parse(state: State<String>): SapClientData {
        var keys = mutableMapOf<String, String>()
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
        return SapClientData(
            action = keys["action"]!!,
            charset = keys["sap-charset"]!!,
            wdSecureId = keys["sap-wd-secure-id"]!!,
            appName = keys["fesrAppName"]!!,
            useBeacon = (keys["fesrUseBeacon"] == "true")
        )
    }
}