package xyz.eatsteak.kusaint.util

import org.brotli.dec.BrotliInputStream

actual fun decompressBrotli(byteArray: ByteArray): ByteArray = BrotliInputStream(byteArray.inputStream()).readAllBytes()