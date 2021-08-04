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

fun HeadersBuilder.appendEccXhrHeaders() {
    append("Accept", "*/*")
    append("Accept-Encoding", "gzip, deflat, br")
    append("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3")
    append("Connection", "keep-alive")
    append("DNT", "1")
    append("Host", "ecc.ssu.ac.kr")
    append("Sec-Fetch-Dest", "empty")
    append("Sec-Fetch-Mode", "cors")
    append("Sec-Fetch-Site", "same-origin")
    append("Sec-GPC", "1")
    append("X-Requested-With", "XMLHttpRequest")
    append("X-XHR-Logon", "accept")
}