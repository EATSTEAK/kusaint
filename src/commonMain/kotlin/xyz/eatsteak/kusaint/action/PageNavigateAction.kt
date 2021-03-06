package xyz.eatsteak.kusaint.action

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite
import xyz.eatsteak.kusaint.state.State

open class PageNavigateAction(private val url: String) : Action<String> {
    override val prerequisite: Prerequisite = Prerequisite.EMPTY

    override suspend fun launch(client: HttpClient, state: State<String>): ActionResult<String> {
        val response = client.get<HttpResponse>(url)
        val str: String = response.receive()
        return ActionResult(this::class, response, str, state.mutations)
    }
}