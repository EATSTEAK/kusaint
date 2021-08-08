package xyz.eatsteak.kusaint.api

import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.action.sap.TimeTableActions
import xyz.eatsteak.kusaint.constant.LineConstant
import xyz.eatsteak.kusaint.constant.PageConstant
import xyz.eatsteak.kusaint.eventqueue.model.SapClient
import xyz.eatsteak.kusaint.model.GraduatedMajorData
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.model.MajorData
import xyz.eatsteak.kusaint.parser.ClientFormParser
import xyz.eatsteak.kusaint.parser.ComboBoxParser
import xyz.eatsteak.kusaint.parser.TimeTableParser
import xyz.eatsteak.kusaint.state.State

class TimeTableApi(val stateSupplier: suspend () -> State<String>) {

    inner class Major {
        suspend fun find(
            year: Int,
            semester: String,
            collage: String,
            department: String,
            major: String? = null
        ): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.Major().selectCollage(
                    collages[collage] ?: throw IllegalArgumentException("Cannot find collage. possible values: $collages")
                )
            )
            val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.Major().selectDepartment(
                    departments[department]
                        ?: throw IllegalArgumentException("Cannot find department. possible values: $departments")
                )
            )
            if (major != null) {
                val majors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR).parse(eccState)
                eccState.mutate(
                    sapClient.TimeTableActions.Major().searchWithMajor(
                        majors[major] ?: throw IllegalArgumentException("Cannot find major. possible values: $majors")
                    )
                )
            } else {
                eccState.mutate(sapClient.TimeTableActions.Major().search())
            }
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(year: Int, semester: String): Map<MajorData, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<MajorData, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE).parse(eccState)
            collages.entries.forEach { (collage, collageKey) ->
                eccState.mutate(sapClient.TimeTableActions.Major().selectCollage(collageKey))
                val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT).parse(eccState)
                departments.entries.forEach { (department, departmentKey) ->
                    eccState.mutate(sapClient.TimeTableActions.Major().selectDepartment(departmentKey))
                    val majors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR).parse(eccState)
                    if (majors.size > 1) {
                        majors.forEach { (major, majorKey) ->
                            eccState.mutate(sapClient.TimeTableActions.Major().searchWithMajor(majorKey))
                            ret[MajorData(collage, department, major)] = TimeTableParser.parse(eccState)
                        }
                    } else {
                        eccState.mutate(sapClient.TimeTableActions.Major().search())
                        ret[MajorData(collage, department, department)] = TimeTableParser.parse(eccState)
                    }
                }
            }
            return ret
        }
    }

    inner class RequiredElective {
        suspend fun find(year: Int, semester: String, lectureName: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.REQUIRED_ELECTIVE))
            val requiredElectives = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_REQUIRED_ELECTIVE).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.RequiredElective().search(
                    requiredElectives[lectureName]
                        ?: throw IllegalArgumentException("Cannot find lecture name. possible values: $requiredElectives")
                )
            )
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<String, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.REQUIRED_ELECTIVE))
            val requiredElectives = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_REQUIRED_ELECTIVE).parse(eccState)
            requiredElectives.forEach { (elective, electiveKey) ->
                sapClient.TimeTableActions.RequiredElective().search(electiveKey)
                ret[elective] = TimeTableParser.parse(eccState)
            }
            return ret
        }
    }

    inner class OptionalElective {
        suspend fun find(year: Int, semester: String, category: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.OPTIONAL_ELECTIVE))
            val categories = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_OPTIONAL_ELECTIVE).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.OptionalElective().search(
                    categories[category]
                        ?: throw IllegalArgumentException("Cannot find category. possible values: $categories")
                )
            )
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<String, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.OPTIONAL_ELECTIVE))
            val categories = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_OPTIONAL_ELECTIVE).parse(eccState)
            categories.forEach { (category, categoryKey) ->
                sapClient.TimeTableActions.OptionalElective().search(categoryKey)
                ret[category] = TimeTableParser.parse(eccState)
            }
            return ret
        }
    }

    inner class Chapel {

        suspend fun find(year: Int, semester: String, lectureName: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.CHAPEL))
            val chapelElectives = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_CHAPEL).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.Chapel().search(
                    chapelElectives[lectureName]
                        ?: throw IllegalArgumentException("Cannot find lecture name. possible values: $chapelElectives")
                )
            )
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<String, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.CHAPEL))
            val chapelElectives = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_CHAPEL).parse(eccState)
            chapelElectives.forEach { (elective, electiveKey) ->
                sapClient.TimeTableActions.Chapel().search(electiveKey)
                ret[elective] = TimeTableParser.parse(eccState)
            }
            return ret
        }
    }

    inner class Teaching {

        suspend fun find(year: Int, semester: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.TEACHING))
            eccState.mutate(sapClient.TimeTableActions.Teaching().search())
            return TimeTableParser.parse(eccState)
        }

    }

    inner class ExtendedCollage {
        suspend fun find(year: Int, semester: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.EXTENDED_COLLAGE))
            eccState.mutate(sapClient.TimeTableActions.ExtendedCollage().search())
            return TimeTableParser.parse(eccState)
        }
    }

    inner class StandardSelection {
        suspend fun find(year: Int, semester: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.STANDARD_SELECTION))
            eccState.mutate(sapClient.TimeTableActions.StandardSelection().search())
            return TimeTableParser.parse(eccState)
        }
    }

    inner class GraduatedSchool {
        suspend fun find(
            year: Int,
            semester: String,
            school: String,
            department: String
        ): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            val schools = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_GRADUATED_SCHOOL).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.GraduatedSchool().selectGraduatedSchool(
                    schools[school] ?: throw IllegalArgumentException("Cannot find school. possible values: $schools")
                )
            )
            val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.GraduatedSchool().searchWithDepartment(
                    departments[department]
                        ?: throw IllegalArgumentException("Cannot find department. possible values: $departments")
                )
            )
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(
            year: Int,
            semester: String
        ): Map<GraduatedMajorData, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<GraduatedMajorData, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            val schools = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_GRADUATED_SCHOOL).parse(eccState)
            schools.forEach { (school, schoolKey) ->
                eccState.mutate(sapClient.TimeTableActions.GraduatedSchool().selectGraduatedSchool(schoolKey))
                val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL).parse(eccState)
                departments.forEach { (department, departmentKey) ->
                    eccState.mutate(sapClient.TimeTableActions.GraduatedSchool().searchWithDepartment(departmentKey))
                    ret[GraduatedMajorData(school, department)] = TimeTableParser.parse(eccState)
                }
            }
            return ret
        }
    }

    inner class LinkedMajor {
        suspend fun find(year: Int, semester: String, major: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.LINKED_MAJOR))
            val linkedMajors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_LINKED_MAJOR).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.LinkedMajor().search(
                    linkedMajors[major]
                        ?: throw IllegalArgumentException("Cannot find lecture name. possible values: $linkedMajors")
                )
            )
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<String, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.LINKED_MAJOR))
            val linkedMajors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_LINKED_MAJOR).parse(eccState)
            linkedMajors.forEach { (linkedMajor, linkedMajorKey) ->
                sapClient.TimeTableActions.LinkedMajor().search(linkedMajorKey)
                ret[linkedMajor] = TimeTableParser.parse(eccState)
            }
            return ret
        }
    }

    inner class CombinedMajor {
        suspend fun find(year: Int, semester: String, major: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.COMBINED_MAJOR))
            val combinedMajors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COMBINED_MAJOR).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.CombinedMajor().search(
                    combinedMajors[major]
                        ?: throw IllegalArgumentException("Cannot find lecture name. possible values: $combinedMajors")
                )
            )
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<String, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.COMBINED_MAJOR))
            val combinedMajors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COMBINED_MAJOR).parse(eccState)
            combinedMajors.forEach { (combinedMajor, combinedMajorKey) ->
                sapClient.TimeTableActions.CombinedMajor().search(combinedMajorKey)
                ret[combinedMajor] = TimeTableParser.parse(eccState)
            }
            return ret
        }
    }

    inner class FindByProfessorName {
        suspend fun find(year: Int, semester: String, professorName: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.FIND_BY_PROFESSOR_NAME))
            eccState.mutate(sapClient.TimeTableActions.FindByProfessorName().search(professorName))
            return TimeTableParser.parse(eccState)
        }
    }

    inner class FindByLecture {
        suspend fun find(year: Int, semester: String, lecture: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.FIND_BY_LECTURE))
            eccState.mutate(sapClient.TimeTableActions.FindByLecture().search(lecture))
            return TimeTableParser.parse(eccState)
        }
    }

    inner class RecognizedOtherMajor {
        suspend fun find(
            year: Int,
            semester: String,
            collage: String,
            department: String,
            major: String? = null
        ): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.RECOGNIZED_OTHER_MAJOR))
            val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.RecognizedOtherMajor().selectCollage(
                    collages[collage] ?: throw IllegalArgumentException("Cannot find collage. possible values: $collages")
                )
            )
            val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.RecognizedOtherMajor().selectDepartment(
                    departments[department]
                        ?: throw IllegalArgumentException("Cannot find department. possible values: $departments")
                )
            )
            if (major != null) {
                val majors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR).parse(eccState)
                eccState.mutate(
                    sapClient.TimeTableActions.RecognizedOtherMajor().searchWithMajor(
                        majors[major] ?: throw IllegalArgumentException("Cannot find major. possible values: $majors")
                    )
                )
            } else {
                eccState.mutate(sapClient.TimeTableActions.RecognizedOtherMajor().search())
            }
            return TimeTableParser.parse(eccState)
        }

        suspend fun all(year: Int, semester: String): Map<MajorData, Collection<LectureData>> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val ret = mutableMapOf<MajorData, Collection<LectureData>>()
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.RECOGNIZED_OTHER_MAJOR))
            val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR).parse(eccState)
            collages.entries.forEach { (collage, collageKey) ->
                eccState.mutate(sapClient.TimeTableActions.RecognizedOtherMajor().selectCollage(collageKey))
                val departments = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR).parse(eccState)
                departments.entries.forEach { (department, departmentKey) ->
                    eccState.mutate(sapClient.TimeTableActions.RecognizedOtherMajor().selectDepartment(departmentKey))
                    val majors = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR).parse(eccState)
                    if (majors.size > 1) {
                        majors.forEach { (major, majorKey) ->
                            eccState.mutate(sapClient.TimeTableActions.RecognizedOtherMajor().searchWithMajor(majorKey))
                            ret[MajorData(collage, department, major)] = TimeTableParser.parse(eccState)
                        }
                    } else {
                        eccState.mutate(sapClient.TimeTableActions.RecognizedOtherMajor().search())
                        ret[MajorData(collage, department, department)] = TimeTableParser.parse(eccState)
                    }
                }
            }
            return ret
        }
    }

    inner class DualListing {
        suspend fun find(year: Int, semester: String): Collection<LectureData> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.initialSemesterSelection(sapClient, year, semester)
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.DUAL_LISTING))
            eccState.mutate(sapClient.TimeTableActions.DualListing().search())
            return TimeTableParser.parse(eccState)
        }
    }

    private suspend fun State<String>.initialSemesterSelection(sapClient: SapClient, year: Int, semester: String) {
        mutate(sapClient.TimeTableActions.Common().initialLoad())
        val semesters = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_SEMESTER).parse(this)
        mutate(sapClient.TimeTableActions.Common().selectYear("$year"))
        mutate(
            sapClient.TimeTableActions.Common().selectSemester(
                semesters[semester] ?: throw IllegalArgumentException(
                    "Cannot find semester. possible values: $semesters"
                )
            )
        )
        mutate(sapClient.TimeTableActions.Common().changeLineNumber(LineConstant.FIVE_HUNDRED.value))
    }
}