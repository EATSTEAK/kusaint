package xyz.eatsteak.kusaint.constant

import io.ktor.http.*

fun HeadersBuilder.appendEccHeaders() {
    append("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    append("Accept-Encoding", "gzip, deflate, br")
    append("Cache-Control", "max-age=0")
    append("Connection", "keep-alive")
    append("DNT", "1")
    append("Host", "ecc.ssu.ac.kr")
    append("Sec-Fetch-Dest", "document")
    append("Sec-Fetch-Mode", "navigate")
    append("Sec-Fetch-Site", "cross-site")
    append("Sec-GPC", "1")
    append("Upgrade-Insecure-Requests", "1")
}