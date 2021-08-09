package xyz.eatsteak.kusaint.state

import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult
import xyz.eatsteak.kusaint.parser.Parser

interface State<T> {

    suspend fun mutate(action: Action<T>): State<T>

    suspend fun <R> parse(parser: Parser<T, R>): R = parser.parse(this)

    val mutations: List<ActionResult<T>>
}