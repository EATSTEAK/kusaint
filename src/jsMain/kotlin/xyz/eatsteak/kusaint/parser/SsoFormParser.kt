package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.state.State

actual object SsoFormParser : Parser<String, SsoForm> {
    actual override suspend fun parse(state: State<String>): SsoForm {
        TODO("Not yet implemented")
    }
}