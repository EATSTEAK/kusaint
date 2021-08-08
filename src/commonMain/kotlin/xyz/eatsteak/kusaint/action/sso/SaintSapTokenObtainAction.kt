package xyz.eatsteak.kusaint.action.sso

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult
import xyz.eatsteak.kusaint.action.prerequisite.Prerequisite
import xyz.eatsteak.kusaint.action.prerequisite.PrerequisiteStrategy
import xyz.eatsteak.kusaint.constant.appendSsoHeaders

class SaintSapTokenObtainAction(val id: String): Action<String> {
    override val prerequisite: Prerequisite
        get() = Prerequisite(PrerequisiteStrategy.CONTAINS, listOf(SsoLoginAction::class))

    override suspend fun launch(client: HttpClient, mutations: List<ActionResult<String>>): ActionResult<String> {
        val sTokenCookie = client.cookies("https://saint.ssu.ac.kr").find { it.name == "sToken" }?.value ?: throw IllegalStateException("Cannot find sToken, Is sso login succeed?")
        val response = client.get<HttpResponse>("https://saint.ssu.ac.kr/webSSO/sso.jsp") {
            headers {
                appendSsoHeaders()
            }
            parameter("sToken", sTokenCookie)
            parameter("sIdno", id)
        }
        return ActionResult(this::class, response, response.receive(), mutations)
    }

}