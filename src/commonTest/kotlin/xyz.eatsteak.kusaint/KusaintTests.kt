package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.eventqueue.toEventQueueString
import xyz.eatsteak.kusaint.parser.ClientFormParser
import xyz.eatsteak.kusaint.state.StateBuilders
import kotlin.test.Test
import kotlin.test.assertEquals

class KusaintTests {

    @Test
    fun eventQueueStringConversionTest() {
        val test = listOf(
            "https://ecc.ssu.ac.kr/sap/bc/webdynpro/sap/zcmw2100?sap-language=KO#",
            "@{\"iSelectionStart\":0,\"iSelectionEnd\":0,\"iCursorPos\":0,\"sValue\":\"2 학기\",\"sFocussedId\":\"WDDC\",\"sApplyControlId\":\"WDDC\"}"
        )
        val result = listOf(
            "https~003A~002F~002Fecc.ssu.ac.kr~002Fsap~002Fbc~002Fwebdynpro~002Fsap~002Fzcmw2100~003Fsap-language~003DKO~0023",
            "~0040~007B~0022iSelectionStart~0022~003A0~002C~0022iSelectionEnd~0022~003A0~002C~0022iCursorPos~0022~003A0~002C~0022sValue~0022~003A~00222~0020~D559~AE30~0022~002C~0022sFocussedId~0022~003A~0022WDDC~0022~002C~0022sApplyControlId~0022~003A~0022WDDC~0022~007D"
        )
        test.forEachIndexed { index, it -> assertEquals(result[index], it.toEventQueueString()) }
    }

    @Test
    fun clientFormParseTest() {
        runBlockingTest {
            val eccState = StateBuilders.ECC.build()
            eccState.mutate(TimeTablePageNavigateAction)
            ClientFormParser.parse(eccState)
        }
    }
}