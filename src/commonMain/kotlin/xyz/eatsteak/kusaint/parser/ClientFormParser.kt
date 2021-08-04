package xyz.eatsteak.kusaint.parser

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import xyz.eatsteak.kusaint.eventqueue.model.SapClientData
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.decompressBrotli

expect object ClientFormParser: Parser<String, SapClientData> {
    override suspend fun parse(state: State<String>): SapClientData
}