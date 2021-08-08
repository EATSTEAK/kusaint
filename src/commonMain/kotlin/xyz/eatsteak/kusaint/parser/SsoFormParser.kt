package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.eventqueue.model.SapClient
import xyz.eatsteak.kusaint.state.State

expect object SsoFormParser: Parser<String, SsoForm> {
    override suspend fun parse(state: State<String>): SsoForm
}

data class SsoForm(
    val inTpBit: String,
    val rqstCausCd: String
)