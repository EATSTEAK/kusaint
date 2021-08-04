package xyz.eatsteak.kusaint

import io.ktor.client.call.*
import xyz.eatsteak.kusaint.action.SapEventQueueAction
import xyz.eatsteak.kusaint.action.SapInitialUpdateAction
import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.eventqueue.*
import xyz.eatsteak.kusaint.parser.ClientFormParser
import xyz.eatsteak.kusaint.parser.TimeTableParser
import xyz.eatsteak.kusaint.state.BasicState
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.state.StateBuilders
import xyz.eatsteak.kusaint.util.decompressBrotli

object Kusaint {
    suspend fun getTimeTable() {
        val eccState: State<String> = StateBuilders.ECC.apply {
            addMutations(TimeTablePageNavigateAction)
        }.build()
        val sapClientData = ClientFormParser.parse(eccState)
        println("[INFO] START MUTATION.")
        println("""
            ===== NEXT MUTATION - LOADING PLACEHOLDER =====
        """.trimIndent())
        val loadingPlaceholder = EventQueueBuilder() {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "ClientWidth:568px;ClientHeight:815px;ScreenWidth:1440px;ScreenHeight:900px;ScreenOrientation:landscape;ThemedTableRowHeight:33px;ThemedFormLayoutRowHeight:32px;ThemedSvgLibUrls:{\"SAPGUI-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPGUI-icons.svg\",\"SAPWeb-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPWeb-icons.svg\"~007D;ThemeTags:Fiori_3,Touch;SapThemeID:sap_fiori_3;DeviceType:DESKTOP"))
            addEvent(ClientInspectorEventBuilders.notify("WD02", "ThemedTableRowHeight:25px"))
            addEvent(LoadingPlaceHolderEventBuilders.load())
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, loadingPlaceholder))
        println("""
            ===== NEXT MUTATION - SELECT SEMESTER =====
        """.trimIndent())
        val selectSemester = EventQueueBuilder() {
            addEvent(CustomEventBuilders.clientInfos("WD01"))
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select("WDDD", "092"))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, selectSemester))
        println("""
            ===== NEXT MUTATION - SELECT COLLAGE =====
        """.trimIndent())
        val selectCollage = EventQueueBuilder() {
            addEvent(ComboBoxEventBuilders.select("WDFA", "11000037"))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, selectCollage))
        println("""
            ===== NEXT MUTATION - SELECT MAJOR =====
        """.trimIndent())
        val selectMajor = EventQueueBuilder() {
            addEvent(ComboBoxEventBuilders.select("WD0107", "11000039"))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, selectMajor))
        println("""
            ===== NEXT MUTATION - SEARCH =====
        """.trimIndent())
        val search = EventQueueBuilder() {
            addEvent(ButtonEventBuilders.press("WD010D"))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
        eccState.mutate(SapEventQueueAction("https://ecc.ssu.ac.kr", sapClientData, search))
        val resultHtml = eccState.mutations.last().result
        val resultAction = eccState.mutations.last().action
        TimeTableParser.parse(eccState)
    }
}