package xyz.eatsteak.kusaint.action.sap

import xyz.eatsteak.kusaint.constant.PageConstant
import xyz.eatsteak.kusaint.eventqueue.*
import xyz.eatsteak.kusaint.eventqueue.model.SapClient


class CommonActions(private val sapClient: SapClient) {
}

class TimeTableActions(private val sapClient: SapClient) {

    inner class Common {
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
            addEvent(ComboBoxEventBuilders.select(PageConstant.Common.COMBOBOX_LINENUMBER, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        // 2021 - 2021학년도
        fun selectYear(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(CustomEventBuilders.clientInfos("WD01"))
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_YEAR, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        // 092 - 2 학기
        fun selectSemester(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(CustomEventBuilders.clientInfos("WD01"))
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_SEMESTER, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        fun selectTab(item: PageConstant.TimeTable.TabItem) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(TabStripEventBuilders.select(PageConstant.TimeTable.TAB_ID, item.itemId, item.itemIndex, item.firstItemIndex))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class Major {
        // 11000037 - IT 대학
        fun selectCollage(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_COLLAGE, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        // 11000039 - 글로벌미디어학부
        fun selectDepartment(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_DEPARTMENT, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        // 11000080 - 정보통신전자공학부
        fun searchWithMajor(majorKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_MAJOR, majorKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_MAJOR))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        fun search() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_MAJOR))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class RequiredElective {
        fun search(electiveKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_REQUIRED_ELECTIVE, electiveKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_REQUIRED_ELECTIVE))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class OptionalElective {
        fun search(electiveKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_OPTIONAL_ELECTIVE, electiveKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_OPTIONAL_ELECTIVE))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class Chapel {
        fun search(electiveKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_CHAPEL, electiveKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_CHAPEL))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class Teaching {
        fun search() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_TEACHING))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class ExtendedCollage {
        fun search() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_EXTENDED_COLLAGE))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class StandardSelection {
        fun search() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_STANDARD_SELECTION))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class GraduatedSchool {

        fun selectGraduatedSchool(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_GRADUATED_SCHOOL, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        fun searchWithDepartment(departmentKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL, departmentKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_GRADUATED_SCHOOL))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class LinkedMajor {
        fun search(majorKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_LINKED_MAJOR, majorKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_LINKED_MAJOR))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class CombinedMajor {
        fun search(majorKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_COMBINED_MAJOR, majorKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_COMBINED_MAJOR))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class FindByProfessorName {
        fun search(professorName: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.change(PageConstant.TimeTable.COMBOBOX_FIND_BY_PROFESSOR_NAME, professorName))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_FIND_BY_PROFESSOR_NAME))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class FindByLecture {
        fun search(lecture: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ComboBoxEventBuilders.change(PageConstant.TimeTable.COMBOBOX_FIND_BY_LECTURE, lecture))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_FIND_BY_LECTURE))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class RecognizedOtherMajor {
        // 11000037 - IT 대학
        fun selectCollage(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        // 11000039 - 글로벌미디어학부
        fun selectDepartment(key: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR, key))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        // 11000080 - 정보통신전자공학부
        fun searchWithMajor(majorKey: String) = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ComboBoxEventBuilders.select(PageConstant.TimeTable.COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR, majorKey))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_RECOGNIZED_OTHER_MAJOR))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }

        fun search() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_RECOGNIZED_OTHER_MAJOR))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }

    inner class DualListing {
        fun search() = SapEventQueueAction("https://ecc.ssu.ac.kr", sapClient) {
            addEvent(ClientInspectorEventBuilders.notify("WD01", "SapThemeID:sap_fiori_3"))
            addEvent(ButtonEventBuilders.press(PageConstant.TimeTable.BUTTON_SEARCH_DUAL_LISTING))
            addEvent(FormEventBuilders.request("sap.client.SsrClient.form"))
        }
    }


}

val SapClient.TimeTableActions: TimeTableActions
    get() = TimeTableActions(this)