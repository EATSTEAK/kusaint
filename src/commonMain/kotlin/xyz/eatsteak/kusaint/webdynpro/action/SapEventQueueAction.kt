package xyz.eatsteak.kusaint.webdynpro.action

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite
import xyz.eatsteak.kusaint.constant.appendEccXhrHeaders
import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventQueueBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.SapClient
import xyz.eatsteak.kusaint.webdynpro.parser.ClientFormParser
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.updatePage

class SapEventQueueAction(
    private val baseUrl: String,
    private var sapClient: SapClient? = null,
    private val eventQueue: EventQueueBuilder
) :
    Action<String> {

    override val prerequisite: Prerequisite = Prerequisite.EMPTY

    constructor(baseUrl: String, sapClient: SapClient? = null, block: EventQueueBuilder.() -> Unit) : this(
        baseUrl,
        sapClient,
        EventQueueBuilder(block)
    )

    override suspend fun launch(client: HttpClient, state: State<String>): ActionResult<String> {
        val sap = if(sapClient == null) ClientFormParser.parse(state) else sapClient!!
        val response = client.post<HttpResponse>(baseUrl + sap.action) {
            headers {
                appendEccXhrHeaders()
            }
            body = FormDataContent(Parameters.build {
                append("sap-charset", sap.charset)
                append("sap-wd-secure-id", sap.wdSecureId)
                append("fesrAppName", sap.appName)
                append("fesrUseBeacon", sap.useBeacon.toString())
                append("SAPEVENTQUEUE", eventQueue.build())
            })
        }
        val decompressed: String = response.receive()
        // println("[INFO] Try to Update Page by update requests.")
        val updatedDocument: String = updatePage(state.mutations.last().result, decompressed)
        return ActionResult(this::class, response, updatedDocument, state.mutations)
    }
}