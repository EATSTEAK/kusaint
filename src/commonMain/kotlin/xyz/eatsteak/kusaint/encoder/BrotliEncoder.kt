package xyz.eatsteak.kusaint.encoder

import io.ktor.client.features.compression.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.eatsteak.kusaint.util.decompressBrotli

expect object BrotliEncoder: ContentEncoder {
}