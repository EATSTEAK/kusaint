package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.action.sap.*
import xyz.eatsteak.kusaint.constant.LineConstant
import xyz.eatsteak.kusaint.constant.PageConstant
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.model.MajorData
import xyz.eatsteak.kusaint.parser.ClientFormParser
import xyz.eatsteak.kusaint.parser.ComboBoxParser
import xyz.eatsteak.kusaint.parser.TimeTableParser
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.state.States

object Kusaint {
    suspend fun getTimeTableForMajor(year: Int, semester: String, collage: String, department: String, major: String? = null): Pair<MajorData, Collection<LectureData>> {
        val eccState: State<String> = States.ECC().apply {
            mutate(TimeTablePageNavigateAction)
        }
        val sapClient = ClientFormParser.parse(eccState)
        eccState.mutate(sapClient.CommonActions.initialLoad())
        val semesters = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_SEMESTER).parse(eccState)
        val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE).parse(eccState)
        eccState.mutate(sapClient.TimeTableActions.selectYear("$year"))
        eccState.mutate(sapClient.TimeTableActions.selectSemester(semesters[semester] ?: throw IllegalArgumentException("Cannot find semester. possible values: $semesters")))
        eccState.mutate(sapClient.CommonActions.changeLineNumber(LineConstant.FIVE_HUNDRED.value))
        eccState.mutate(sapClient.TimeTableActions.selectCollage(collages[collage] ?: throw IllegalArgumentException("Cannot find collage. possible values: $collages")))
        val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT).parse(eccState)
        eccState.mutate(sapClient.TimeTableActions.selectDepartment(departments[department] ?: throw IllegalArgumentException("Cannot find department. possible values: $departments")))
        if(major != null) {
            val majors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR).parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.searchWithMajor(majors[major] ?: throw IllegalArgumentException("Cannot find major. possible values: $majors")))
        } else {
            eccState.mutate(sapClient.TimeTableActions.search())
        }
        return MajorData(collage, department, major ?: department) to TimeTableParser.parse(eccState)
    }

    suspend fun getTimeTableForAllMajors(year: Int, semester: String): Map<MajorData, Collection<LectureData>> {
        val eccState: State<String> = States.ECC().apply {
            mutate(TimeTablePageNavigateAction)
        }
        val ret = mutableMapOf<MajorData, Collection<LectureData>>()
        val sapClient = ClientFormParser.parse(eccState)
        eccState.mutate(sapClient.CommonActions.initialLoad())
        val semesters = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_SEMESTER).parse(eccState)
        val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE).parse(eccState)
        eccState.mutate(sapClient.TimeTableActions.selectYear("$year"))
        eccState.mutate(sapClient.TimeTableActions.selectSemester(semesters[semester] ?: throw IllegalArgumentException("Cannot find semester. possible values: $semesters")))
        eccState.mutate(sapClient.CommonActions.changeLineNumber(LineConstant.FIVE_HUNDRED.value))
        collages.entries.forEach { (collage, collageKey) ->
            eccState.mutate(sapClient.TimeTableActions.selectCollage(collageKey))
            val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT).parse(eccState)
            departments.entries.forEach { (department, departmentKey) ->
                eccState.mutate(sapClient.TimeTableActions.selectDepartment(departmentKey))
                val majors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR).parse(eccState)
                if(majors.size > 1) {
                    majors.forEach { (major, majorKey) ->
                        eccState.mutate(sapClient.TimeTableActions.searchWithMajor(majorKey))
                        ret[MajorData(collage, department, major)] = TimeTableParser.parse(eccState)
                    }
                } else {
                    ret[MajorData(collage, department, department)] = TimeTableParser.parse(eccState)
                }
            }
        }
        return ret
    }
}