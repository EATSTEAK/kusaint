package xyz.eatsteak.kusaint.webdynpro.parser

import io.ktor.http.*
import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import xyz.eatsteak.kusaint.parser.Parser
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.JSDOM
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.SapClient

actual object ClientFormParser: Parser<String, SapClient> {

    actual override suspend fun parse(state: State<String>): SapClient {
        val keys = mutableMapOf<String, String>()
        state.mutations.forEach { actionRes ->
            if(actionRes.response.contentType().toString().startsWith("text/html")) {
                val doc = JSDOM(actionRes.result).window.document
                val clientElem = doc.querySelector("#sap\\.client\\.SsrClient\\.form")
                if(clientElem != null) {
                    keys["action"] = clientElem.getAttribute("action")!! as String
                    (clientElem.children as ItemArrayLike<dynamic>).asList().forEach {
                        keys[(it.id as String)] = (it.getAttribute("value")!! as String)
                    }
                }
            }
        }
        val sapClient = SapClient(
            action = keys["action"]!!,
            charset = keys["sap-charset"]!!,
            wdSecureId = keys["sap-wd-secure-id"]!!,
            appName = keys["fesrAppName"]!!,
            useBeacon = (keys["fesrUseBeacon"] == "true")
        )
        return sapClient
    }
}