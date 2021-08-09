package xyz.eatsteak.kusaint.parser

import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.state.State

actual object TimeTableParser : Parser<String, Collection<LectureData>> {
    actual override suspend fun parse(state: State<String>): Collection<LectureData> {
        val lastData = state.mutations.last().result
        val doc = Jsoup.parse(lastData)
        val tableElem =
            doc.select("#WD017C-contentTBody").first() ?: throw IllegalStateException("Cannot find table body.")
        val headerElem = tableElem.children().first() ?: throw IllegalStateException("Cannot find table headers.")
        val dataElems = tableElem.children().subList(1, tableElem.children().size)
        val headers = headerElem.children()
            .map { it.select("[id$=\"-arialabel\"]").forEach { aria -> aria.remove() }; it.text().trim() }
        return dataElems.map {
            it.children().mapIndexed { index, element -> headers[index] to element.text().trim() }.toMap()
                .asLectureData()
        }
    }
}
