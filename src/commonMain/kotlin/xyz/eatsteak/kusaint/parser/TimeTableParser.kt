package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.state.State

expect object TimeTableParser : Parser<String, Collection<LectureData>> {
    override suspend fun parse(state: State<String>): Collection<LectureData>
}

internal fun Map<String, String>.asLectureData(): LectureData = LectureData(
    getOrElse("계획") { "" },
    getOrElse("이수구분(주전공)") { "" },
    getOrElse("이수구분(다전공)") { "" },
    getOrElse("공학인증") { "" },
    getOrElse("교과영역") { "" },
    getOrElse("과목번호") { "" },
    getOrElse("과목명") { "" },
    getOrElse("분반") { "" },
    getOrElse("교수명") { "" },
    getOrElse("개설학과") { "" },
    getOrElse("시간/학점(설계)") { "" },
    getOrElse("수강인원") { "" },
    getOrElse("여석") { "" },
    getOrElse("강의시간(강의실)") { "" }
)