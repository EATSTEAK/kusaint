package xyz.eatsteak.kusaint.state

import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import xyz.eatsteak.kusaint.action.PageNavigateAction
import xyz.eatsteak.kusaint.constant.appendEccHeaders

object StateBuilders {

    val ECC = StateBuilder(BasicState() {
        defaultRequest {
            headers { appendEccHeaders() }
        }
        install(HttpCookies)
        BrowserUserAgent()
    })
}