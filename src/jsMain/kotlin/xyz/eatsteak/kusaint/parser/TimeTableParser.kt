package xyz.eatsteak.kusaint.parser

import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.JSDOM

actual object TimeTableParser : Parser<String, Collection<LectureData>> {

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    actual override suspend fun parse(state: State<String>): Collection<LectureData> {
        val doc = JSDOM(state.mutations.last().result).window.document
        val tableElem = doc.querySelector("#WD017C-contentTBody") ?: throw IllegalStateException("Cannot find table body.")
        val headerElem = tableElem.children.item(0) ?: throw IllegalStateException("Cannot find table headers.")
        val dataElems = (tableElem.children as ItemArrayLike<dynamic>).asList().run { subList(1, size) }
        val headers = (headerElem.children as ItemArrayLike<dynamic>).asList().mapNotNull {
            (it.querySelectorAll("[id$=\"-arialabel\"]") as ItemArrayLike<dynamic>).asList().forEach { aria ->
                aria.remove()
            }
            (it.textContent as String?)?.trim()
        }
        val data = dataElems.map {
            (it.children as ItemArrayLike<dynamic>).asList().mapIndexed { index, element ->
                headers[index] to (element.textContent as String).trim()
            }.toMap().asLectureData()
        }
        return data
    }
}