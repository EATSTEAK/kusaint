package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.eventqueue.EventQueueBuilder
import xyz.eatsteak.kusaint.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.eventqueue.model.UcfCardinality
import xyz.eatsteak.kusaint.eventqueue.model.UcfResponseData
import xyz.eatsteak.kusaint.eventqueue.toEventQueueString
import kotlin.test.Test
import kotlin.test.assertEquals

class EventQueueTests {

    /*
    Test Conversion of normal string to the event string.
     */
    @Test
    fun eventStringConversionTest() {
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

    /*
    Test EventQueueStringBuilder.
     */
    @Test
    fun eventQueueBuildTest() {
        val test = listOf(
            EventQueueBuilder {
                add("Custom", "ClientInfos") {
                    parameter("Id", "WD01")
                    parameter("WindowOpenerExists", "false")
                    parameter("ClientURL", "https://ecc.ssu.ac.kr/sap/bc/webdynpro/sap/zcmw2100?sap-language=KO#")
                    parameter("ClientWidth", "1440")
                    parameter("ClientHeight", "790")
                    parameter("DocumentDomain", "ssu.ac.kr")
                    parameter("IsTopWindow", "true")
                    parameter("ParentAccessible", "true")

                    ucfParameter {
                        action = UcfAction.ENQUEUE
                        responseData = UcfResponseData.DELTA
                    }
                }
                add("ClientInspector", "Notify") {
                    parameter("Id", "WD01")
                    parameter("Data", "SapThemeID:sap_fiori_3")

                    ucfParameter {
                        responseData = UcfResponseData.DELTA
                        enqueueCardinality = UcfCardinality.SINGLE
                    }
                }
                add("ComboBox", "Select") {
                    parameter("Id", "WDDD")
                    parameter("Key", "092")
                    parameter("ByEnter", "false")

                    ucfParameter {
                        responseData = UcfResponseData.DELTA
                        action = UcfAction.SUBMIT
                    }
                }
                add("Form", "Request") {
                    parameter("Id", "sap.client.SsrClient.form")
                    parameter("Async", "false")
                    parameter("FocusInfo", "@{\"iSelectionStart\":0,\"iSelectionEnd\":0,\"iCursorPos\":0,\"sValue\":\"2 학기\",\"sFocussedId\":\"WDDD\",\"sApplyControlId\":\"WDDD\"}")
                    parameter("Hash", "")
                    parameter("DomChanged", "false")
                    parameter("IsDirty", "false")

                    ucfParameter {
                        responseData = UcfResponseData.DELTA
                    }
                }
            }
        )
        val result = listOf(
            "Custom_ClientInfos~E002Id~E004WD01~E005WindowOpenerExists~E004false~E005ClientURL~E004https~003A~002F~002Fecc.ssu.ac.kr~002Fsap~002Fbc~002Fwebdynpro~002Fsap~002Fzcmw2100~003Fsap-language~003DKO~0023~E005ClientWidth~E0041440~E005ClientHeight~E004790~E005DocumentDomain~E004ssu.ac.kr~E005IsTopWindow~E004true~E005ParentAccessible~E004true~E003~E002ClientAction~E004enqueue~E005ResponseData~E004delta~E003~E002~E003~E001ClientInspector_Notify~E002Id~E004WD01~E005Data~E004SapThemeID~003Asap_fiori_3~E003~E002EnqueueCardinality~E004single~E005ResponseData~E004delta~E003~E002~E003~E001ComboBox_Select~E002Id~E004WDDD~E005Key~E004092~E005ByEnter~E004false~E003~E002ClientAction~E004submit~E005ResponseData~E004delta~E003~E002~E003~E001Form_Request~E002Id~E004sap.client.SsrClient.form~E005Async~E004false~E005FocusInfo~E004~0040~007B~0022iSelectionStart~0022~003A0~002C~0022iSelectionEnd~0022~003A0~002C~0022iCursorPos~0022~003A0~002C~0022sValue~0022~003A~00222~0020~D559~AE30~0022~002C~0022sFocussedId~0022~003A~0022WDDD~0022~002C~0022sApplyControlId~0022~003A~0022WDDD~0022~007D~E005Hash~E004~E005DomChanged~E004false~E005IsDirty~E004false~E003~E002ResponseData~E004delta~E003~E002~E003"
        )
        test.forEachIndexed { index, builder -> assertEquals(result[index], builder.build()) }
    }
}