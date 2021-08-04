package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.state.State

actual object TimeTableParser : Parser<String, Collection<LectureData>> {
    actual override suspend fun parse(state: State<String>): Collection<LectureData> {
        return listOf()
    }
}