@file:Suppress("unused")

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

/**
 * Obtain TimeTable data from u-saint.
 * **Warning: This will not work with authentication! Use anonymous state for now.**
 * @property stateSupplier state supplier for methods, use [xyz.eatsteak.kusaint.Kusaint] for easy use. If you want more advanced usage, please refer to [xyz.eatsteak.kusaint.state.States].
 */
class TimeTableApi(val stateSupplier: suspend () -> State<String>) {

    inner class Major {

        /**
         * Find lectures of selected major.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param collage desired collage name to find.
         * @param department desired department name to find.
         * @param major desired major name to find. If only one major is exist for the department. then this value can be null.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
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

        /**
         * Find lectures of all majors.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of [xyz.eatsteak.kusaint.model.MajorData] paired with collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
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

        /**
         * Gets a list of available collages.
         * @return Collection of collage names in String.
         */
        suspend fun getCollages(): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE).parse(eccState).keys
        }

        /**
         * Gets a list of available departments of given collage.
         * @param collage desired collage name.
         * @return Collection of department names in String.
         */
        suspend fun getDepartments(collage: String): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            val collages = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COLLAGE).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.Major().selectCollage(
                    collages[collage] ?: throw IllegalArgumentException("Cannot find collage. possible values: $collages")
                )
            )
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT).parse(eccState).keys
        }

        /**
         * Gets a list of available majors of given collage and department.
         * @param collage desired collage name.
         * @param department desired department name.
         * @return Collection of major names in String.
         */
        suspend fun getMajors(collage: String, department: String): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
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
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_MAJOR).parse(eccState).keys
        }
    }

    inner class RequiredElective {

        /**
         * Find lectures of required elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param lectureName desired lectureName to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find all lectures of required elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LectureName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Gets a list of available lecture names in required electives.
         * @return Collection of lecture names in String.
         */
        suspend fun getLectureName(): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.REQUIRED_ELECTIVE))
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_REQUIRED_ELECTIVE).parse(eccState).keys
        }

    }

    inner class OptionalElective {

        /**
         * Find lectures of optional elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param category desired category to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find all lectures of optional elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of Category paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Gets a list of available categories in optional electives.
         * @return Collection of categories in String.
         */
        suspend fun getCategories(): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.OPTIONAL_ELECTIVE))
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_OPTIONAL_ELECTIVE).parse(eccState).keys
        }
    }

    inner class Chapel {

        /**
         * Find chapel lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param lectureName desired lectureName to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find all chapel lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LectureName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Gets a list of available lecture names in chapels.
         * @return Collection of lecture names in String.
         */
        suspend fun getLectureNames(): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.CHAPEL))
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_CHAPEL).parse(eccState).keys
        }
    }

    inner class Teaching {

        /**
         * Find teaching(교직) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find extended collage(평생교육사) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find standard selection lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find graduated school's lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param school desired school category to find.
         * @param department desired department to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find all graduated school's lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of [xyz.eatsteak.kusaint.model.GraduatedMajorData] paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Gets a list of graduated schools.
         * @return Collection of graduated school names in String.
         */
        suspend fun getGraduatedSchools(): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_GRADUATED_SCHOOL).parse(eccState).keys
        }

        /**
         * Gets a list of departments in given graduated school.
         * @return Collection of departments in String.
         */
        suspend fun getDepartments(school: String): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            val schools = ComboBoxParser(PageConstant.TimeTable.COMBOBOX_GRADUATED_SCHOOL).parse(eccState)
            eccState.mutate(
                sapClient.TimeTableActions.GraduatedSchool().selectGraduatedSchool(
                    schools[school] ?: throw IllegalArgumentException("Cannot find school. possible values: $schools")
                )
            )
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL).parse(eccState).keys
        }
    }

    inner class LinkedMajor {

        /**
         * Find linked major(연계전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param major desired linked major to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find all linked major(연계전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LinkedMajorName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Gets a list of available linked majors.
         * @return Collection of major names in String.
         */
        suspend fun getLinkedMajor(): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.LINKED_MAJOR))
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_LINKED_MAJOR).parse(eccState).keys
        }
    }

    inner class CombinedMajor {

        /**
         * Find combined major(융합전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param major desired combined major to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find all combined major(융합전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of CombinedMajorName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Gets a list of available combined majors.
         * @return Collection of major names in String.
         */
        suspend fun getCombinedMajor(): Collection<String> {
            val eccState: State<String> = stateSupplier().apply {
                mutate(TimeTablePageNavigateAction)
            }
            val sapClient = ClientFormParser.parse(eccState)
            eccState.mutate(sapClient.TimeTableActions.Common().initialLoad())
            eccState.mutate(sapClient.TimeTableActions.Common().selectTab(PageConstant.TimeTable.TabItem.COMBINED_MAJOR))
            return ComboBoxParser(PageConstant.TimeTable.COMBOBOX_COMBINED_MAJOR).parse(eccState).keys
        }
    }

    inner class FindByProfessorName {

        /**
         * Find lectures by professor's name.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param professorName desired professor name to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find lectures by lecture name.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param lecture desired lecture name to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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

        /**
         * Find recognized lectures of selected major.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param collage desired collage name to find.
         * @param department desired department name to find.
         * @param major desired major name to find. If only one major is exist for the department. then this value can be null.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
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

        /**
         * Find recognized lectures of all majors.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of [xyz.eatsteak.kusaint.model.MajorData] paired with collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
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

        /**
         * Find dual listing lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
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