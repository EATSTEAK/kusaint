package xyz.eatsteak.kusaint.action.eventqueue

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
import xyz.eatsteak.kusaint.eventqueue.EventQueueBuilder
import xyz.eatsteak.kusaint.eventqueue.model.SapClient
import xyz.eatsteak.kusaint.util.decompressBrotli
import xyz.eatsteak.kusaint.util.updatePage

class SapEventQueueAction(private val baseUrl: String, private val sapClient: SapClient, private val eventQueue: EventQueueBuilder):
    Action<String> {
    override val prerequisite: Prerequisite = Prerequisite.EMPTY

    constructor(baseUrl: String, sapClient: SapClient, block: EventQueueBuilder.() -> Unit): this(baseUrl, sapClient, EventQueueBuilder(block))

    override suspend fun launch(client: HttpClient, mutations: List<ActionResult<String>>): ActionResult<String> {
        val response = client.post<HttpResponse>(baseUrl + sapClient.action) {
            headers {
                appendEccXhrHeaders()
            }
            body = FormDataContent(Parameters.build {
                append("sap-charset", sapClient.charset)
                append("sap-wd-secure-id", sapClient.wdSecureId)
                append("fesrAppName", sapClient.appName)
                append("fesrUseBeacon", sapClient.useBeacon.toString())
                append("SAPEVENTQUEUE", eventQueue.build())
            })
        }
        val decompressed = decompressBrotli(response.receive())
        println("[INFO] Try to Update Page by update requests.")
        val updatedDocument: String = updatePage(mutations.last().result, decompressed.decodeToString())
        return ActionResult(this::class, response, updatedDocument, mutations)
    }
}