package xyz.eatsteak.kusaint.action

import io.ktor.client.*
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite
import xyz.eatsteak.kusaint.state.State

interface Action<T> {

    val prerequisite: Prerequisite

    suspend fun launch(client: HttpClient, state: State<T>): ActionResult<T>

}