package xyz.eatsteak.kusaint.action.sap

import xyz.eatsteak.kusaint.constant.PageConstant
import xyz.eatsteak.kusaint.eventqueue.*


object CommonActions {
}

object TimeTableActions {

    fun initialLoad() = SapEventQueueAction("https://ecc.ssu.ac.kr") {
        add(
            ClientInspectorEventBuilders.notify(
                "WD01",
                "ClientWidth:568px;ClientHeight:815px;ScreenWidth:1440px;ScreenHeight:900px;ScreenOrientation:landscape;ThemedTableRowHeight:33px;ThemedFormLayoutRowHeight:32px;ThemedSvgLibUrls:{\"SAPGUI-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPGUI-icons.svg\",\"SAPWeb-icons\":\"https://ecc.ssu.ac.kr/sap/public/bc/themes/~00257eclient-100/~00257ecache-e7HcFzUVz0vqzuQYwI4n6rPh2g4/Base/baseLib/sap_fiori_3/svg/libs/SAPWeb-icons.svg\"~007D;ThemeTags:Fiori_3,Touch;SapThemeID:sap_fiori_3;DeviceType:DESKTOP"
            )
        )
        add(ClientInspectorEventBuilders.notify("WD02", "ThemedTableRowHeight:25px"))
        add(LoadingPlaceHolderEventBuilders.load())
        add(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    fun selectOption(id: String, key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr") {
        add(ComboBoxEventBuilders.select(id, key))
        add(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    fun selectOptionWithNotify(id: String, key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr") {
        add(CustomEventBuilders.clientInfos("WD01"))
        add(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
        add(ComboBoxEventBuilders.select(id, key))
        add(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    fun selectTab(id: String, item: PageConstant.TimeTable.TabItem) =
        SapEventQueueAction("https://ecc.ssu.ac.kr") {
            add(TabStripEventBuilders.select(id, item.itemId, item.itemIndex, item.firstItemIndex))
            add(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

    fun pressButtonWithOption(comboBoxId: String, key: String, buttonId: String) =
        SapEventQueueAction("https://ecc.ssu.ac.kr") {
            add(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            add(ComboBoxEventBuilders.select(comboBoxId, key))
            add(ButtonEventBuilders.press(buttonId))
            add(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

    fun pressButton(id: String) = SapEventQueueAction("https://ecc.ssu.ac.kr") {
        add(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
        add(ButtonEventBuilders.press(id))
        add(FormEventBuilders.request("sap.client.SsrClient.form"))
    }

    fun pressButtonWithChange(comboBoxId: String, value: String, buttonId: String) =
        SapEventQueueAction("https://ecc.ssu.ac.kr") {
            add(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            add(ComboBoxEventBuilders.change(comboBoxId, value))
            add(ButtonEventBuilders.press(buttonId))
            add(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

}