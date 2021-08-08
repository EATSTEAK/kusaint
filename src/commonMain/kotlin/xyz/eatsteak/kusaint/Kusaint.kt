package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.action.sap.*
import xyz.eatsteak.kusaint.constant.LineConstant
import xyz.eatsteak.kusaint.constant.PageConstant
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.parser.ClientFormParser
import xyz.eatsteak.kusaint.parser.ComboBoxParser
import xyz.eatsteak.kusaint.parser.TimeTableParser
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.state.States

object Kusaint {
    suspend fun getTimeTable(year: Int, semester: String, collage: String, department: String, lineNumber: LineConstant = LineConstant.FIVE_HUNDRED, major: String? = null): Collection<LectureData> {
        val eccState: State<String> = States.ECC().apply {
            mutate(TimeTablePageNavigateAction)
        }
        val sapClient = ClientFormParser.parse(eccState)
        eccState.mutate(sapClient.CommonActions.initialLoad())
        val semesters = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_SEMESTER).parse(eccState)
        val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE).parse(eccState)
        eccState.mutate(sapClient.TimeTableActions.selectYear("$year"))
        eccState.mutate(sapClient.TimeTableActions.selectSemester(semesters[semester] ?: throw IllegalArgumentException("Cannot find semester. possible values: $semesters")))
        eccState.mutate(sapClient.CommonActions.changeLineNumber(lineNumber.value))
        eccState.mutate(sapClient.TimeTableActions.selectCollage(collages[collage] ?: throw IllegalArgumentException("Cannot find collage. possible values: $collages")))
        val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT).parse(eccState)
        eccState.mutate(sapClient.TimeTableActions.selectDepartment(departments[department] ?: throw IllegalArgumentException("Cannot find department. possible values: $departments")))
        if(major != null) {
            val majors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR).parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.searchWithMajor(majors[major] ?: throw IllegalArgumentException("Cannot find major. possible values: $majors")))
        } else {
            eccState.mutate(sapClient.TimeTableActions.search())
        }
        return TimeTableParser.parse(eccState)
    }
}