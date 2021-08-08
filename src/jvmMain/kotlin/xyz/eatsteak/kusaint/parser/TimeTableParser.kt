package xyz.eatsteak.kusaint.parser

import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.state.State

actual object TimeTableParser : Parser<String, Collection<LectureData>> {
    actual override suspend fun parse(state: State<String>): Collection<LectureData> {
        val lastData = state.mutations.last().result
        val doc = Jsoup.parse(lastData)
        val tableElem = doc.select("#WD017C-contentTBody").first() ?: throw IllegalStateException("Cannot find table body.")
        val headerElem = tableElem.children().first()
        val dataElems = tableElem.children().subList(1, tableElem.children().size)
        val headers = headerElem!!.children().map { it.select("[id$=\"-arialabel\"]").forEach { aria -> aria.remove() }; it.text().trim() }
        val data = dataElems.map { it.children().mapIndexed { index, element -> Pair(headers[index], element.text().trim()) }.toMap().asLectureData() }
        return data
    }
}

private fun  Map<String, String>.asLectureData(): LectureData = LectureData(
    getOrDefault("계획", ""),
    getOrDefault("이수구분(주전공)", ""),
    getOrDefault("이수구분(다전공)", ""),
    getOrDefault("공학인증", ""),
    getOrDefault("교과영역", ""),
    getOrDefault("과목번호", ""),
    getOrDefault("과목명", ""),
    getOrDefault("분반", ""),
    getOrDefault("교수명", ""),
    getOrDefault("개설학과", ""),
    getOrDefault("시간/학점(설계)", ""),
    getOrDefault("수강인원", ""),
    getOrDefault("여석", ""),
    getOrDefault("강의시간(강의실)", "")
)
