package xyz.eatsteak.kusaint.webdynpro.parser

import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.JSDOM
import xyz.eatsteak.kusaint.webdynpro.component.ComboBox

actual class ComboBoxParser actual constructor(override val component: ComboBox) : ComponentParser<ComboBox, Map<String, String>> {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    actual override suspend fun parse(state: State<String>): Map<String, String> {
        val latestDoc = JSDOM(state.mutations.last().result).window.document
        val comboBoxElem = latestDoc.querySelector("[id=\"${component.id}\"]") ?: throw IllegalArgumentException("Cannot find comboBox.")
        val listId = comboBoxElem.getAttribute("aria-controls")
        val listElem = latestDoc.querySelector("[id=\"$listId\"]") ?: throw IllegalStateException("Cannot find combobox item list.")
        val listValuesElem = listElem.querySelector(".lsListbox__values") ?: throw IllegalStateException("Cannot find combobox item list values.")
        val ret = mutableMapOf<String, String>()
        (listValuesElem.children as ItemArrayLike<dynamic>).asList().forEach {
            val key = it.textContent as String?
            val value = it.getAttribute("data-itemkey") as String?
            if(key != null && value != null) ret[key] = value
        }
        return ret
    }

}