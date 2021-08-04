package xyz.eatsteak.kusaint.eventqueue

fun customClientInfos(id: String) = EventBuilder("Custom_ClientInfos") {
    putFirst("Id", id)
    putFirst("WindowOpenerExists", "false")
    putFirst("ClientURL", "https://ecc.ssu.ac.kr/sap/bc/webdynpro/sap/zcmw2100?sap-language=KO#")
    putFirst("ClientWidth", "1440")
    putFirst("ClientHeight", "790")
    putFirst("DocumentDomain", "ssu.ac.kr")
    putFirst("IsTopWindow", "true")
    putFirst("ParentAccessible", "true")

    putSecond("ClientAction", "enqueue")
    putSecond("ResponseData", "delta")
}

fun clientInspectorNotify(id: String) = EventBuilder("ClientInspector_Notify") {
    putFirst("Id", "WD01")
    putFirst("Data", "SapThemeID:sap_fiori_3")

    putSecond("ResponseData", "delta")
    putSecond("EnqueueCardinality", "single")
}

fun messageAreaReposition(id: String, top: String, left: String) = EventBuilder("MessageArea_Reposition") {
    putFirst("Id", id)
    putFirst("Top", top)
    putFirst("Left", left)

    putSecond("ResponseData", "delta")
    putSecond("EnqueueCardinality", "single")
}

fun comboBoxSelect(id: String, key: String) = EventBuilder("ComboBox_Select") {
    putFirst("Id", id)
    putFirst("Key", key)
    putFirst("ByEnter", "false")

    putSecond("ResponseData", "delta")
    putSecond("ClientAction", "submit")
}

fun buttonPress(id: String) = EventBuilder("Button_Press") {
    putFirst("Id", id)

    putSecond("ResponseData", "delta")
    putSecond("ClientAction", "submit")
}

fun formRequest(id: String) = EventBuilder("Form_Request") {
    putFirst("Id", id)
    putFirst("Async", "false")
    putFirst("Hash", "")
    putFirst("DomChanged", "false")
    putFirst("IsDirty", "false")

    putSecond("ResponseData", "delta")
}