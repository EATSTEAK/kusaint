package xyz.eatsteak.kusaint.state

import io.ktor.client.*
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult

class BasicState(requestConfig: HttpClientConfig<*>.() -> Unit): State<String> {
    val cookieStorage = AcceptAllCookiesStorage()

    private val client = HttpClient(requestConfig).config {
        install(HttpCookies) {
            storage = cookieStorage
        }
    }

    private val actions: MutableList<ActionResult<String>> = mutableListOf()

    override val mutations: List<ActionResult<String>>
        get() {
            return actions
        }

    override suspend fun mutate(action: Action<String>): BasicState {
        if(!action.prerequisite.isMatched(mutations)) throw IllegalStateException("This action cannot be applied for current state.")
        val currentMutations = actions.toList()
        val result = action.launch(client, currentMutations)
        actions.add(result)
        return this
    }




}