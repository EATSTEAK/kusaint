package xyz.eatsteak.kusaint

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class JsTests {


    @Test
    fun jsFetchTest() = runBlockingTest {
        val client = HttpClient()
        val response = client.post<HttpResponse>("https://smartid.ssu.ac.kr/Symtra_sso/smln_pcs.asp")
        println(response.receive())
    }
}