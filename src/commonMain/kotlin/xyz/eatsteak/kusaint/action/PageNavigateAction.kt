package xyz.eatsteak.kusaint.action

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite

open class PageNavigateAction(private val url: String): Action<HttpResponse> {
    override val prerequisite: Prerequisite = Prerequisite.EMPTY

    override suspend fun launch(client: HttpClient, mutations: List<ActionResult<HttpResponse>>) = client.get<HttpResponse>(url)
}