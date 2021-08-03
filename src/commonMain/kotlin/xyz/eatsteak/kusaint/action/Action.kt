package xyz.eatsteak.kusaint.action

import io.ktor.client.*
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite

interface Action<T> {

    val prerequisite: Prerequisite

    suspend fun launch(client: HttpClient, mutations: List<ActionResult<T>>): T

}