package xyz.eatsteak.kusaint.parser

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.decompressBrotli

object ClientFormParser: Parser<HttpResponse> {
    override suspend fun parse(state: State<HttpResponse>) {
        state.mutations.forEach {
            println(it.result.status)
            val byteArray = it.result.receive<ByteArray>()
            val decompressed = decompressBrotli(byteArray)
            println(decompressed.decodeToString())
        }
    }
}