@file:OptIn(ExperimentalIoApi::class)

package xyz.eatsteak.kusaint.encoder

import io.ktor.client.features.compression.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.CoroutineScope
import org.brotli.dec.BrotliInputStream
import java.nio.ByteBuffer

internal actual object BrotliEncoder : ContentEncoder {
    override val name: String
        get() = "br"

    override fun CoroutineScope.decode(source: ByteReadChannel): ByteReadChannel {
        val inputStream = source.toInputStream()
        return BrotliInputStream(inputStream).toByteReadChannel()
    }

    override fun CoroutineScope.encode(source: ByteReadChannel): ByteReadChannel {
        TODO("Not yet implemented")
    }
}