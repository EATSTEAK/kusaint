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
        val response = client.get<HttpResponse>("https://saint.ssu.ac.kr")
        println(response.receive())
    }
}