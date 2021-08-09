package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.credentials.MyCredentials
import xyz.eatsteak.kusaint.state.States
import kotlin.test.BeforeTest
import kotlin.test.Test

class KusaintTests {

    @BeforeTest
    fun initialize() {
        println(PlatformInit.isReady)
    }

    @Test
    fun ssoLoginTest() = runBlockingTest {
        States.eccAuthenticatedState(MyCredentials.id, MyCredentials.password)()
    }

}