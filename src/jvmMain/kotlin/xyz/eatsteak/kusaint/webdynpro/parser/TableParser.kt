package xyz.eatsteak.kusaint.webdynpro.parser

import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.webdynpro.component.Table

actual class TableParser actual constructor(override val component: Table) : ComponentParser<Table, Collection<Map<String, String>>> {
    actual override suspend fun parse(state: State<String>): Collection<Map<String, String>> {
        val lastData = state.mutations.last().result
        val doc = Jsoup.parse(lastData)
        val tableElem =
            doc.select("[id=\"${component.id}\"]").first() ?: throw IllegalStateException("Cannot find table body.")
        val headerElem = tableElem.children().first() ?: throw IllegalStateException("Cannot find table headers.")
        val dataElems = tableElem.children().subList(1, tableElem.children().size)
        val headers = headerElem.children()
            .map { it.select("[id$=\"-arialabel\"]").forEach { aria -> aria.remove() }; it.text().trim() }
        return dataElems.map {
            it.children().mapIndexed { index, element -> headers[index] to element.text().trim() }.toMap()
        }
    }
}