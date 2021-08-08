package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.state.State

expect object TimeTableParser : Parser<String, Collection<LectureData>> {
    override suspend fun parse(state: State<String>): Collection<LectureData>
}