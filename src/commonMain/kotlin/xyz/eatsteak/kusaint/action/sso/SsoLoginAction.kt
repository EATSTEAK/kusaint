package xyz.eatsteak.kusaint.action.sso

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite
import xyz.eatsteak.kusaint.constant.appendSsoHeaders
import xyz.eatsteak.kusaint.parser.SsoForm
import xyz.eatsteak.kusaint.state.State

class SsoLoginAction(val ssoForm: SsoForm, val id: String, val password: String) : Action<String> {
    override val prerequisite: Prerequisite = Prerequisite.EMPTY


    override suspend fun launch(client: HttpClient, state: State<String>): ActionResult<String> {
        val response = client.post<HttpResponse>("https://smartid.ssu.ac.kr/Symtra_sso/smln_pcs.asp") {
            headers {
                appendSsoHeaders()
            }
            body = FormDataContent(Parameters.build {
                append("in_tp_bit", ssoForm.inTpBit)
                append("rqst_caus_cd", ssoForm.rqstCausCd)
                append("userid", id)
                append("pwd", password)
            })
        }
        return ActionResult(this::class, response, state.mutations.last().result, state.mutations)
    }
}