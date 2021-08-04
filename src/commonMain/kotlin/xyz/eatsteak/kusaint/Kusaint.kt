package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.action.eventqueue.*
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.parser.ClientFormParser
import xyz.eatsteak.kusaint.parser.TimeTableParser
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.state.StateBuilders

object Kusaint {
    suspend fun getTimeTable(year: Int, semester: String, collage: String, department: String, major: String, lineNumber: Int): Collection<LectureData> {
        val eccState: State<String> = StateBuilders.ECC.apply {
            addMutations(TimeTablePageNavigateAction)
        }.build()
        val sapClient = ClientFormParser.parse(eccState)
        eccState.mutate(sapClient.CommonActions.initialLoad())
        eccState.mutate(sapClient.TimeTableActions.selectSemester("092"))
        eccState.mutate(sapClient.CommonActions.changeLineNumber("500"))
        eccState.mutate(sapClient.TimeTableActions.selectCollage("11000037"))
        eccState.mutate(sapClient.TimeTableActions.selectDepartment("11000039"))
        eccState.mutate(sapClient.TimeTableActions.search())
        return TimeTableParser.parse(eccState)
    }
}