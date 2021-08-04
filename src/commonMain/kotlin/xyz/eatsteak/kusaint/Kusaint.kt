package xyz.eatsteak.kusaint

import io.ktor.client.call.*
import xyz.eatsteak.kusaint.action.SapEventQueueAction
import xyz.eatsteak.kusaint.action.SapInitialUpdateAction
import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.eventqueue.*
import xyz.eatsteak.kusaint.parser.ClientFormParser
import xyz.eatsteak.kusaint.state.BasicState
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.state.StateBuilders
import xyz.eatsteak.kusaint.util.decompressBrotli

object Kusaint {
    suspend fun getTimeTable() {
        val eccState: State<String> = StateBuilders.ECC.apply {
            addMutations(TimeTablePageNavigateAction)
        }.build()
        var sapClientData = ClientFormParser.parse(eccState)
        println("[INFO] START MUTATION.")
        val loadingPlaceholder = EventQueueBuilder() {
            addEvent(clientInspectorNotify("WD01", "ClientWidth:568px;ClientHeight:815px;ScreenWidth:1440px;ScreenHeight:900px;ScreenOrientation:landscape;ThemedTableRowHeight:33px;ThemedFormLayoutRowHeight:32px;ThemedSvgLibUrls:{\"SAPGUI-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPGUI-icons.svg\",\"SAPWeb-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPWeb-icons.svg\"~007D;ThemeTags:Fiori_3,Touch;SapThemeID:sap_fiori_3;DeviceType:DESKTOP"))
            addEvent(clientInspectorNotify("WD02", "ThemedTableRowHeight:25px"))
            addEvent(loadingPlaceHolderLoad())
            addEvent(formRequest("sap.client.SsrClient.form"))
        }
        println("""
            ===== NEXT MUTATION - LOADING PLACEHOLDER =====
        """.trimIndent())
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, loadingPlaceholder))
        val selectSemester = EventQueueBuilder() {
            addEvent(customClientInfos("WD01"))
            addEvent(clientInspectorNotify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(comboBoxSelect("WDDD", "092"))
            addEvent(formRequest("sap.client.SsrClient.form"))
        }
        sapClientData = ClientFormParser.parse(eccState)
        println("""
            ===== NEXT MUTATION - SELECT SEMESTER =====
        """.trimIndent())
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, selectSemester))
        val selectCollage = EventQueueBuilder() {
            addEvent(comboBoxSelect("WDFA", "11000037"))
            addEvent(formRequest("sap.client.SsrClient.form"))
        }
        sapClientData = ClientFormParser.parse(eccState)
        println("""
            ===== NEXT MUTATION - SELECT COLLAGE =====
        """.trimIndent())
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, selectCollage))
        val selectMajor = EventQueueBuilder() {
            addEvent(comboBoxSelect("WD0107", "11000039"))
            addEvent(formRequest("sap.client.SsrClient.form"))
        }
        sapClientData = ClientFormParser.parse(eccState)
        println("""
            ===== NEXT MUTATION - SELECT MAJOR =====
        """.trimIndent())
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, selectMajor))
        val search = EventQueueBuilder() {
            addEvent(buttonPress("WD010D"))
            addEvent(formRequest("sap.client.SsrClient.form"))
        }
        sapClientData = ClientFormParser.parse(eccState)
        println("""
            ===== NEXT MUTATION - SEARCH =====
        """.trimIndent())
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, search))
        val resultHtml = eccState.mutations.last().result
        val resultAction = eccState.mutations.last().action
        println(resultHtml)
        println(resultAction)
    }
}