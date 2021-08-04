package xyz.eatsteak.kusaint.parser

import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.state.State

actual object TimeTableParser : Parser<String, Collection<LectureData>> {
    actual override suspend fun parse(state: State<String>): Collection<LectureData> {
        val lastData = state.mutations.last().result
        val doc = Jsoup.parse(lastData)
        val tableElem = doc.select("#WD017C-content")
        println(tableElem)
        return listOf()
    }
}