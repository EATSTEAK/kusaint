package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.eventqueue.model.SapClient
import xyz.eatsteak.kusaint.state.State

expect object ClientFormParser : Parser<String, SapClient> {
    override suspend fun parse(state: State<String>): SapClient
}