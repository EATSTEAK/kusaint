package xyz.eatsteak.kusaint.webdynpro.parser

import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.webdynpro.component.ComboBox

actual class ComboBoxParser actual constructor(override val component: ComboBox) : ComponentParser<ComboBox, Map<String, String>> {

    actual override suspend fun parse(state: State<String>): Map<String, String> {
        val latestDoc = Jsoup.parse(state.mutations.last().result)
        val comboBoxElem = latestDoc.select("[id=\"${component.id}\"]").first() ?: throw IllegalArgumentException("Cannot find comboBox.")
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