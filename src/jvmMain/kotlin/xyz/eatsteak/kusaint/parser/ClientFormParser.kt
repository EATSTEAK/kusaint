package xyz.eatsteak.kusaint.parser

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.eventqueue.model.SapClientData
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.decompressBrotli
import kotlin.text.decodeToString

actual object ClientFormParser : Parser<String, SapClientData> {
    actual override suspend fun parse(state: State<String>): SapClientData {
        var keys = mutableMapOf<String, String>()
        state.mutations.forEach { actionRes ->
            val doc = Jsoup.parse(actionRes.result).parser(if(actionRes.response.contentType().toString() == "text/html") org.jsoup.parser.Parser.htmlParser() else org.jsoup.parser.Parser.xmlParser())
            val clientElem = doc.select("[id=\"sap.client.SsrClient.form\"]").first()
            if(clientElem != null) {
                keys["action"] = clientElem.attr("action")

                clientElem.children().forEach {
                    keys[it.id()] = it.attr("value")
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