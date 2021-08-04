package xyz.eatsteak.kusaint.action

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite
import xyz.eatsteak.kusaint.constant.appendEccXhrHeaders
import xyz.eatsteak.kusaint.eventqueue.model.SapClient
import xyz.eatsteak.kusaint.util.decompressBrotli
import xyz.eatsteak.kusaint.util.updatePage

class SapInitialUpdateAction(private val baseUrl: String, private val sapClient: SapClient):
    Action<String> {
    override val prerequisite: Prerequisite = Prerequisite.EMPTY

    override suspend fun launch(client: HttpClient, mutations: List<ActionResult<String>>): ActionResult<String> {
        val response = client.post<HttpResponse>(baseUrl + sapClient.action) {
            headers {
                appendEccXhrHeaders()
            }
        }
        val decompressed = decompressBrotli(response.receive())
        println("[INFO] Try to Update Page by update requests.")
        val updatedDocument: String = updatePage(mutations.last().result, decompressed.decodeToString())
        return ActionResult(this::class, response, updatedDocument, mutations)
    }
}