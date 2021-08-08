package xyz.eatsteak.kusaint.state

import io.ktor.client.features.*
import io.ktor.client.features.compression.*
import io.ktor.client.request.*
import xyz.eatsteak.kusaint.action.PageNavigateAction
import xyz.eatsteak.kusaint.action.sso.SaintSapTokenObtainAction
import xyz.eatsteak.kusaint.action.sso.SsoLoginAction
import xyz.eatsteak.kusaint.constant.appendEccHeaders
import xyz.eatsteak.kusaint.encoder.BrotliEncoder
import xyz.eatsteak.kusaint.parser.SsoFormParser

object States {

    fun eccAnonymousState(): suspend () -> State<String> = {
        BasicState {
            defaultRequest {
                headers { appendEccHeaders() }
            }
            ContentEncoding {
                gzip()
                deflate()
                customEncoder(BrotliEncoder)
            }
            BrowserUserAgent()
        }
    }

    fun eccAuthenticatedState(id: String, password: String): suspend () -> State<String> = {
        val state = BasicState {
            defaultRequest {
                headers { appendEccHeaders() }
            }
            ContentEncoding {
                gzip()
                deflate()
                customEncoder(BrotliEncoder)
            }
            BrowserUserAgent()
        }
        state.mutate(PageNavigateAction("https://smartid.ssu.ac.kr"))
        val ssoForm = SsoFormParser.parse(state)
        state.mutate(SsoLoginAction(ssoForm, id, password))
        state.mutate(SsoLoginAction(ssoForm, id, password))
        state.mutate(SaintSapTokenObtainAction(id))
        state
    }
}