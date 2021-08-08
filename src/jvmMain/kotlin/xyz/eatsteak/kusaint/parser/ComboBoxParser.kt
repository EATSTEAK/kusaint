package xyz.eatsteak.kusaint.parser

import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.state.State

actual class ComboBoxParser actual constructor(private val comboBoxId: String) : Parser<String, Map<String, String>> {

    actual override suspend fun parse(state: State<String>): Map<String, String> {
        val latestDoc = Jsoup.parse(state.mutations.last().result)
        val comboBoxElem = latestDoc.select("[id=\"$comboBoxId\"]").first() ?: throw IllegalArgumentException("Cannot find comboBox.")
        val listId = comboBoxElem.attr("aria-controls")
        val listElem = latestDoc.select("[id=\"$listId\"]").first() ?: throw IllegalStateException("Cannot find combobox item list.")
        val listValuesElem = listElem.select(".lsListbox__values").first() ?: throw IllegalStateException("Cannot find combobox item list values.")
        val ret = mutableMapOf<String, String>()
        listValuesElem.children().forEach {
            ret[it.text()] = it.attr("data-itemkey")
        }
        return ret
    }

}