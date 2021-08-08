package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.credentials.MyCredentials
import xyz.eatsteak.kusaint.state.States
import kotlin.test.Test
import kotlin.test.assertEquals

class KusaintTests {

    @Test
    fun ssoLoginTest() = runBlockingTest {
        States.eccAuthenticatedState(MyCredentials.id, MyCredentials.password)()
    }

}