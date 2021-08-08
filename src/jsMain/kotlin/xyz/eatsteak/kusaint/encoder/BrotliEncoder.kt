package xyz.eatsteak.kusaint.encoder

import io.ktor.client.features.compression.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope

actual object BrotliEncoder : ContentEncoder {
    override val name: String
        get() = "br"

    override fun CoroutineScope.decode(source: ByteReadChannel): ByteReadChannel {
        TODO("Not yet implemented")
    }

    override fun CoroutineScope.encode(source: ByteReadChannel): ByteReadChannel {
        TODO("Not yet implemented")
    }
}