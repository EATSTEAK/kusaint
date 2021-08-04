package xyz.eatsteak.kusaint.util

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

actual fun decompressBrotli(byteArray: ByteArray): ByteArray {
    val decompressed: Uint8Array = decompress(Uint8Array(byteArray.toTypedArray()).buffer)
    val byteArray = ByteArray(decompressed.length)
    for(i in 0 until decompressed.length) {
        byteArray[i] = decompressed[i]
    }
    return byteArray
}

@JsModule("brotli/decompress")
@JsNonModule
external fun decompress(buffer: ArrayBuffer): Uint8Array

