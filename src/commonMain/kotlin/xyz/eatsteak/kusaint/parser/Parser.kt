package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.state.State

interface Parser<T, R> {

    suspend fun parse(state: State<T>): R
}