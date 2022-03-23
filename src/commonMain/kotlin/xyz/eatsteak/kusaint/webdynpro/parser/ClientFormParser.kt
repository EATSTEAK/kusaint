package xyz.eatsteak.kusaint.webdynpro.parser

import xyz.eatsteak.kusaint.parser.Parser
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.SapClient

expect object ClientFormParser : Parser<String, SapClient> {
    override suspend fun parse(state: State<String>): SapClient
}