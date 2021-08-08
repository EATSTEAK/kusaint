package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.constant.LineConstant
import xyz.eatsteak.kusaint.credentials.MyCredentials
import xyz.eatsteak.kusaint.eventqueue.EventQueueBuilder
import xyz.eatsteak.kusaint.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.eventqueue.model.UcfCardinality
import xyz.eatsteak.kusaint.eventqueue.model.UcfResponseData
import xyz.eatsteak.kusaint.eventqueue.toEventQueueString
import xyz.eatsteak.kusaint.state.States
import kotlin.test.Test
import kotlin.test.assertEquals

class KusaintTests {

    @Test
    fun ssoLoginTest() {
        runBlockingTest {
            States.eccAuthenticatedState(MyCredentials.id, MyCredentials.password)()
        }
    }

}