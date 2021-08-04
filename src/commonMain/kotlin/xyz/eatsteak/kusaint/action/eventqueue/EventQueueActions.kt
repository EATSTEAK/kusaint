package xyz.eatsteak.kusaint.action.eventqueue

import xyz.eatsteak.kusaint.eventqueue.*
import xyz.eatsteak.kusaint.eventqueue.model.SapClient


class CommonActions(private val sapClient: SapClient) {
    fun initialLoad() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(
            ClientInspectorEventBuilders.notify(
                "WD01",
                "ClientWidth:568px;ClientHeight:815px;ScreenWidth:1440px;ScreenHeight:900px;ScreenOrientation:landscape;ThemedTableRowHeight:33px;ThemedFormLayoutRowHeight:32px;ThemedSvgLibUrls:{\"SAPGUI-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPGUI-icons.svg\",\"SAPWeb-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPWeb-icons.svg\"~007D;ThemeTags:Fiori_3,Touch;SapThemeID:sap_fiori_3;DeviceType:DESKTOP"
            )
        )
        addEvent(ClientInspectorEventBuilders.notify("WD02", "ThemedTableRowHeight:25px"))
        addEvent(LoadingPlaceHolderEventBuilders.load())
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    // 200 - 200줄
    fun changeLineNumber(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(ComboBoxEventBuilders.select("WD0159", key))
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }
}

class TimeTableActions(private val sapClient: SapClient) {

    // 2021 - 2021학년도
    fun selectYear(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(CustomEventBuilders.clientInfos("WD01"))
        addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
        addEvent(ComboBoxEventBuilders.select("WD89", key))
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    // 092 - 2 학기
    fun selectSemester(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(CustomEventBuilders.clientInfos("WD01"))
        addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
        addEvent(ComboBoxEventBuilders.select("WDDD", key))
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    // 11000037 - IT 대학
    fun selectCollage(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(ComboBoxEventBuilders.select("WDFA", key))
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    // 11000039 - 글로벌미디어학부
    fun selectDepartment(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(ComboBoxEventBuilders.select("WD0107", key))
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    // 11000080 - 정보통신전자공학부
    fun search(majorKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(ComboBoxEventBuilders.select("WD010A", majorKey))
        addEvent(ButtonEventBuilders.press("WD010D"))
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    fun search() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
        addEvent(ButtonEventBuilders.press("WD010D"))
        addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
    }
}

val SapClient.CommonActions: CommonActions
    get() = CommonActions(this)
val SapClient.TimeTableActions: TimeTableActions
    get() = TimeTableActions(this)