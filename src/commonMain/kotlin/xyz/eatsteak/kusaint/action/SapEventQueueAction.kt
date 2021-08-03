package xyz.eatsteak.kusaint.action

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite
import xyz.eatsteak.kusaint.eventqueue.EventQueueBuilder

class SapEventQueueAction(private val baseUrl: String, val action: String, private val charset: String, private val wdSecureId: String, private val appName: String, private val useBeacon: Boolean, private val eventQueue: EventQueueBuilder):
    Action<HttpResponse> {
    override val prerequisite: Prerequisite = Prerequisite.EMPTY

    override suspend fun launch(client: HttpClient, mutations: List<ActionResult<HttpResponse>>): HttpResponse {
        return client.post(baseUrl + action) {
            formData {
                append("sap-charset", charset)
                append("sap-wd-secure-id", wdSecureId)
                append("fesrAppName", appName)
                append("fesrUseBeacon", useBeacon.toString())
                append("SAPEVENTQUEUE", eventQueue.build())
            }
        }
    }
}