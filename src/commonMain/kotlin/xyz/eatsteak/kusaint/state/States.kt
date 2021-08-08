package xyz.eatsteak.kusaint.state

import io.ktor.client.features.*
import io.ktor.client.features.compression.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import xyz.eatsteak.kusaint.action.PageNavigateAction
import xyz.eatsteak.kusaint.action.sso.SaintSapTokenObtainAction
import xyz.eatsteak.kusaint.action.sso.SsoLoginAction
import xyz.eatsteak.kusaint.constant.appendEccHeaders
import xyz.eatsteak.kusaint.encoder.BrotliEncoder
import xyz.eatsteak.kusaint.parser.SsoFormParser

object States {

    val ECC: () -> State<String> = {
        BasicState() {
            defaultRequest {
                headers { appendEccHeaders() }
            }
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
            ContentEncoding {
                gzip()
                deflate()
                customEncoder(BrotliEncoder)
            }
            BrowserUserAgent()
        }
    }

    val ECC_AUTHENTICATED: suspend (id: String, password: String) -> State<String> = { id, pass ->
        val state = BasicState() {
            defaultRequest {
                headers { appendEccHeaders() }
            }
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
            ContentEncoding {
                gzip()
                deflate()
            }
            BrowserUserAgent()
        }
        state.mutate(PageNavigateAction("https://smartid.ssu.ac.kr"))
        val ssoForm = SsoFormParser.parse(state)
        state.mutate(SsoLoginAction(ssoForm, id, pass))
        state.mutate(SaintSapTokenObtainAction(id))
        state
    }
}