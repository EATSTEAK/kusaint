package xyz.eatsteak.kusaint.state

import io.ktor.client.*
import io.ktor.client.statement.*
import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult

class BasicState(requestConfig: HttpClientConfig<*>.() -> Unit): State<HttpResponse> {

    private val client = HttpClient(requestConfig)

    private val actions: MutableList<ActionResult<HttpResponse>> = mutableListOf()

    override val mutations: List<ActionResult<HttpResponse>>
        get() {
            return actions
        }

    override suspend fun mutate(action: Action<HttpResponse>): BasicState {
        if(!action.prerequisite.isMatched(mutations)) throw IllegalStateException("This action cannot be applied for current state.")
        val currentMutations = actions.toList()
        val response = action.launch(client, currentMutations)
        actions.add(ActionResult(action, response, currentMutations))
        return this
    }


}