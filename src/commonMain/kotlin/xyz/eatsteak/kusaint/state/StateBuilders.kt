package xyz.eatsteak.kusaint.state

import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.eatsteak.kusaint.action.PageNavigateAction
import xyz.eatsteak.kusaint.constant.appendEccHeaders

object StateBuilders {

    val ECC: StateBuilder<String> = StateBuilder(BasicState() {
        defaultRequest {
            headers { appendEccHeaders() }
        }
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
            CoroutineScope(Dispatchers.Default).launch { storage.addCookie("ecc.ssu.ac.kr", Cookie("GWMESSENGER", "stopMessenger")) }
        }
        BrowserUserAgent()
    })
}