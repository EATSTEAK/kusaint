package xyz.eatsteak.kusaint.encoder

import io.ktor.client.features.compression.*
import io.ktor.util.*

internal actual object BrotliEncoder : ContentEncoder, Encoder by Identity {
    override val name: String = "br"

}