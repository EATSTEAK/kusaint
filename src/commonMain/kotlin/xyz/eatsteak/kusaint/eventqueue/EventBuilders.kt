package xyz.eatsteak.kusaint.eventqueue

import xyz.eatsteak.kusaint.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.eventqueue.model.UcfCardinality
import xyz.eatsteak.kusaint.eventqueue.model.UcfResponseData


object ClientInspectorEventBuilders {

    fun notify(id: String, data: String) = EventBuilder("ClientInspector", "Notify") {
        parameter("Id", id)
        parameter("Data", data)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            enqueueCardinality = UcfCardinality.SINGLE
        }
    }

}

object CustomEventBuilders {
    fun clientInfos(id: String) = EventBuilder("Custom", "ClientInfos") {
        parameter("Id", id)
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
}

object MessageAreaEventBuilders {
    fun reposition(id: String, top: String, left: String) = EventBuilder("MessageArea", "Reposition") {
        parameter("Id", id)
        parameter("Top", top)
        parameter("Left", left)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            enqueueCardinality = UcfCardinality.SINGLE
        }
    }
}

object ComboBoxEventBuilders {
    fun select(id: String, key: String) = EventBuilder("ComboBox", "Select") {
        parameter("Id", id)
        parameter("Key", key)
        parameter("ByEnter", "false")

        ucfParameter {
            responseData = UcfResponseData.DELTA
            action = UcfAction.SUBMIT
        }
    }
}

object ButtonEventBuilders {
    fun press(id: String) = EventBuilder("Button", "Press") {
        parameter("Id", id)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            action = UcfAction.SUBMIT
        }
    }
}

object TabStripEventBuilders {
    fun select(id: String, itemId: String, itemIndex: Int, firstVisibleItemIndex: Int) =
        EventBuilder("TabStrip", "TabSelect") {
            parameter("Id", id)
            parameter("ItemId", itemId)
            parameter("ItemIndex", itemIndex.toString())
            parameter("FirstVisibleItemIndex", firstVisibleItemIndex.toString())

            ucfParameter {
                responseData = UcfResponseData.DELTA
                action = UcfAction.SUBMIT
            }
        }
}

object FormEventBuilders {
    fun request(id: String) = EventBuilder("Form", "Request") {
        parameter("Id", id)
        parameter("Async", "false")
        parameter("Hash", "")
        parameter("DomChanged", "false")
        parameter("IsDirty", "false")

        ucfParameter {
            responseData = UcfResponseData.DELTA
        }
    }
}

object LoadingPlaceHolderEventBuilders {
    fun load() = EventBuilder("LoadingPlaceHolder", "Load") {
        parameter("Id", "_loadingPlaceholder_")

        ucfParameter {
            responseData = UcfResponseData.DELTA
            action = UcfAction.SUBMIT
        }
    }
}