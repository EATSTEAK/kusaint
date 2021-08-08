package xyz.eatsteak.kusaint.state

import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult

interface State<T> {

    suspend fun mutate(action: Action<T>): State<T>

    val mutations: List<ActionResult<T>>
}