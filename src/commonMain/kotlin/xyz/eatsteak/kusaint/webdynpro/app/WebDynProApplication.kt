package xyz.eatsteak.kusaint.webdynpro.app

import xyz.eatsteak.kusaint.webdynpro.action.SapEventQueueAction
import xyz.eatsteak.kusaint.webdynpro.component.ClientInspector
import xyz.eatsteak.kusaint.webdynpro.component.Custom
import xyz.eatsteak.kusaint.webdynpro.component.Form
import xyz.eatsteak.kusaint.webdynpro.component.LoadingPlaceHolder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventQueueBuilder
import kotlinx.serialization.Serializable

@Serializable
abstract class WebDynProApplication(val baseUrl: String, val appId: String, val description: String = "") {

    private val clientInspectorWD01 = ClientInspector("WD01")
    private val clientInspectorWD02 = ClientInspector("WD02")
    private val clientInspectorWD03 = ClientInspector("WD03")
    private val loadingPlaceHolder = LoadingPlaceHolder("_loadingPlaceholder_")

    private val sapClientForm = Form("sap.client.SsrClient.form")

    private val custom = Custom("WD01")

    private fun createEventQueueWithFormRequest(block: EventQueueBuilder.() -> Unit) = EventQueueBuilder(block) append EventQueueBuilder { add(sapClientForm.request()) }

    private fun createEventQueueWithNotify(block: EventQueueBuilder.() -> Unit) = EventQueueBuilder {
        add(custom.clientInfos("ssu.ac.kr", appUrl))
        add(clientInspectorWD01.notify("SapThemeID:sap_fiori_3"))
    } append createEventQueueWithFormRequest(block)

    fun actionWithFormRequest(block: EventQueueBuilder.() -> Unit) = SapEventQueueAction(baseUrl, null, createEventQueueWithFormRequest(block))
    fun actionWithFormRequest(vararg eventBuilders: EventBuilder) = SapEventQueueAction(baseUrl, null, createEventQueueWithFormRequest { eventBuilders.forEach { add(it) } })
    fun actionWithNotify(block: EventQueueBuilder.() -> Unit) = SapEventQueueAction(baseUrl, null, createEventQueueWithNotify(block))
    fun actionWithNotify(vararg eventBuilders: EventBuilder) = SapEventQueueAction(baseUrl, null, createEventQueueWithNotify { eventBuilders.forEach { add(it) } })

    val appUrl: String
        get() = "$baseUrl/sap/bc/webdynpro/sap/$appId?sap-wd-stableids=x"

    val initialLoad: SapEventQueueAction
        get() = actionWithFormRequest {
        add(
            clientInspectorWD01.notify("ClientWidth:568px;ClientHeight:815px;ScreenWidth:1440px;ScreenHeight:900px;ScreenOrientation:landscape;ThemedTableRowHeight:33px;ThemedFormLayoutRowHeight:32px;ThemedSvgLibUrls:{\"SAPGUI-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPGUI-icons.svg\",\"SAPWeb-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPWeb-icons.svg\"~007D;ThemeTags:Fiori_3,Touch;SapThemeID:sap_fiori_3;DeviceType:DESKTOP")
        )
        add(clientInspectorWD02.notify("ThemedTableRowHeight:25px"))
        add(loadingPlaceHolder.load())
    }
}