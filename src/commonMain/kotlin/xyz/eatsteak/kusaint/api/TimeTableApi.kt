@file:Suppress("unused")

package xyz.eatsteak.kusaint.api

import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.action.sap.TimeTableActions.initialLoad
import xyz.eatsteak.kusaint.action.sap.TimeTableActions.pressButton
import xyz.eatsteak.kusaint.action.sap.TimeTableActions.pressButtonWithChange
import xyz.eatsteak.kusaint.action.sap.TimeTableActions.pressButtonWithOption
import xyz.eatsteak.kusaint.action.sap.TimeTableActions.selectOption
import xyz.eatsteak.kusaint.action.sap.TimeTableActions.selectTab
import xyz.eatsteak.kusaint.constant.LineConstant
import xyz.eatsteak.kusaint.constant.PageConstant
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_CHAPEL
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_COMBINED_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_DUAL_LISTING
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_EXTENDED_COLLAGE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_FIND_BY_LECTURE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_FIND_BY_PROFESSOR_NAME
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_GRADUATED_SCHOOL
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_LINKED_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_OPTIONAL_ELECTIVE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_RECOGNIZED_OTHER_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_REQUIRED_ELECTIVE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_STANDARD_SELECTION
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.BUTTON_SEARCH_TEACHING
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_CHAPEL
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_COLLAGE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_COMBINED_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_DEPARTMENT
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_FIND_BY_LECTURE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_FIND_BY_PROFESSOR_NAME
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_GRADUATED_SCHOOL
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_LINKED_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_OPTIONAL_ELECTIVE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.COMBOBOX_REQUIRED_ELECTIVE
import xyz.eatsteak.kusaint.constant.PageConstant.TimeTable.TAB_ID
import xyz.eatsteak.kusaint.model.GraduatedMajorData
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.model.MajorData
import xyz.eatsteak.kusaint.parser.ComboBoxParser
import xyz.eatsteak.kusaint.parser.TimeTableParser
import xyz.eatsteak.kusaint.state.State

/**
 * Obtain TimeTable data from u-saint.
 * **Warning: This will not work with authentication! Use anonymous state for now.**
 * @property stateSupplier state supplier for methods, use [xyz.eatsteak.kusaint.Kusaint] for easy use. If you want more advanced usage, please refer to [xyz.eatsteak.kusaint.state.States].
 */
class TimeTableApi(val stateSupplier: suspend () -> State<String>) {

    val major by lazy { Major() }

    val requiredElective by lazy { RequiredElective() }

    val optionalElective by lazy { OptionalElective() }

    val chapel by lazy { Chapel() }

    val teaching by lazy { Teaching() }

    val extendedCollage by lazy { ExtendedCollage() }

    val standardSelection by lazy { StandardSelection() }

    val graduatedSchool by lazy { GraduatedSchool() }

    val linkedMajor by lazy { LinkedMajor() }

    val combinedMajor by lazy { CombinedMajor() }

    val findByProfessorName by lazy { FindByProfessorName() }

    val findByLecture by lazy { FindByLecture() }

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
        ): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            findOptionAndSelect(COMBOBOX_COLLAGE, collage)
            findOptionAndSelect(COMBOBOX_DEPARTMENT, department)
            if (major != null) findOptionAndSearch(COMBOBOX_MAJOR, major, BUTTON_SEARCH_MAJOR)
            else mutate(pressButton(BUTTON_SEARCH_MAJOR))
            parse(TimeTableParser)
        }

        /**
         * Find lectures of all majors.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of [xyz.eatsteak.kusaint.model.MajorData] paired with collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
        suspend fun all(year: Int, semester: String): Map<MajorData, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<MajorData, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            findOptionAndEach(COMBOBOX_COLLAGE) { collage, collageKey ->
                mutate(selectOption(COMBOBOX_COLLAGE, collageKey))
                findOptionAndEach(COMBOBOX_DEPARTMENT) { department, departmentKey ->
                    mutate(selectOption(COMBOBOX_DEPARTMENT, departmentKey))
                    findOptionAndEach(COMBOBOX_MAJOR) { major, majorKey ->
                        mutate(
                            pressButtonWithOption(
                                COMBOBOX_MAJOR,
                                majorKey,
                                BUTTON_SEARCH_MAJOR
                            )
                        )
                        ret[MajorData(collage, department, major)] = TimeTableParser.parse(this)
                    }
                }
            }
            ret
        }

        /**
         * Gets a list of available collages.
         * @return Collection of collage names in String.
         */
        suspend fun getCollages(): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            parse(ComboBoxParser(COMBOBOX_COLLAGE)).keys
        }

        /**
         * Gets a list of available departments of given collage.
         * @param collage desired collage name.
         * @return Collection of department names in String.
         */
        suspend fun getDepartments(collage: String): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            findOptionAndSelect(COMBOBOX_COLLAGE, collage)
            parse(ComboBoxParser(COMBOBOX_DEPARTMENT)).keys
        }

        /**
         * Gets a list of available majors of given collage and department.
         * @param collage desired collage name.
         * @param department desired department name.
         * @return Collection of major names in String.
         */
        suspend fun getMajors(collage: String, department: String): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            findOptionAndSelect(COMBOBOX_COLLAGE, collage)
            findOptionAndSelect(COMBOBOX_DEPARTMENT, department)
            parse(ComboBoxParser(COMBOBOX_MAJOR)).keys
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
        suspend fun find(year: Int, semester: String, lectureName: String): Collection<LectureData> =
            stateSupplier().run {
                mutate(TimeTablePageNavigateAction)
                initialSemesterSelection(year, semester)
                mutate(
                    selectTab(
                        TAB_ID,
                        PageConstant.TimeTable.TabItem.REQUIRED_ELECTIVE
                    )
                )
                findOptionAndSearch(COMBOBOX_REQUIRED_ELECTIVE, lectureName, BUTTON_SEARCH_REQUIRED_ELECTIVE)
                parse(TimeTableParser)
            }

        /**
         * Find all lectures of required elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LectureName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(
                selectTab(
                    TAB_ID,
                    PageConstant.TimeTable.TabItem.REQUIRED_ELECTIVE
                )
            )
            findOptionAndEach(COMBOBOX_REQUIRED_ELECTIVE) { elective, electiveKey ->
                pressButtonWithOption(
                    COMBOBOX_REQUIRED_ELECTIVE,
                    electiveKey,
                    BUTTON_SEARCH_REQUIRED_ELECTIVE
                )
                ret[elective] = parse(TimeTableParser)
            }
            ret
        }

        /**
         * Gets a list of available lecture names in required electives.
         * @return Collection of lecture names in String.
         */
        suspend fun getLectureName(): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            mutate(
                selectTab(
                    TAB_ID,
                    PageConstant.TimeTable.TabItem.REQUIRED_ELECTIVE
                )
            )
            parse(ComboBoxParser(COMBOBOX_REQUIRED_ELECTIVE)).keys
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
        suspend fun find(year: Int, semester: String, category: String): Collection<LectureData> =
            stateSupplier().run {
                mutate(TimeTablePageNavigateAction)
                initialSemesterSelection(year, semester)
                mutate(
                    selectTab(
                        TAB_ID,
                        PageConstant.TimeTable.TabItem.OPTIONAL_ELECTIVE
                    )
                )
                findOptionAndSearch(COMBOBOX_OPTIONAL_ELECTIVE, category, BUTTON_SEARCH_OPTIONAL_ELECTIVE)
                parse(TimeTableParser)
            }

        /**
         * Find all lectures of optional elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of Category paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(
                selectTab(
                    TAB_ID,
                    PageConstant.TimeTable.TabItem.OPTIONAL_ELECTIVE
                )
            )
            findOptionAndEach(COMBOBOX_OPTIONAL_ELECTIVE) { elective, electiveKey ->
                pressButtonWithOption(
                    COMBOBOX_OPTIONAL_ELECTIVE,
                    electiveKey,
                    BUTTON_SEARCH_OPTIONAL_ELECTIVE
                )
                ret[elective] = parse(TimeTableParser)
            }
            ret
        }

        /**
         * Gets a list of available categories in optional electives.
         * @return Collection of categories in String.
         */
        suspend fun getCategories(): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            mutate(
                selectTab(
                    TAB_ID, PageConstant.TimeTable.TabItem.OPTIONAL_ELECTIVE
                )
            )
            parse(ComboBoxParser(COMBOBOX_OPTIONAL_ELECTIVE)).keys
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
        suspend fun find(year: Int, semester: String, lectureName: String): Collection<LectureData> =
            stateSupplier().run {
                mutate(TimeTablePageNavigateAction)
                initialSemesterSelection(year, semester)
                mutate(
                    selectTab(
                        TAB_ID,
                        PageConstant.TimeTable.TabItem.CHAPEL
                    )
                )
                findOptionAndSearch(COMBOBOX_CHAPEL, lectureName, BUTTON_SEARCH_CHAPEL)
                parse(TimeTableParser)
            }

        /**
         * Find all chapel lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LectureName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.CHAPEL))
            findOptionAndEach(COMBOBOX_CHAPEL) { elective, electiveKey ->
                mutate(
                    pressButtonWithOption(
                        COMBOBOX_CHAPEL,
                        electiveKey,
                        BUTTON_SEARCH_CHAPEL
                    )
                )
                ret[elective] = parse(TimeTableParser)
            }
            ret
        }

        /**
         * Gets a list of available lecture names in chapels.
         * @return Collection of lecture names in String.
         */
        suspend fun getLectureNames(): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            mutate(
                selectTab(
                    TAB_ID,
                    PageConstant.TimeTable.TabItem.CHAPEL
                )
            )
            parse(ComboBoxParser(COMBOBOX_CHAPEL)).keys
        }
    }

    inner class Teaching {

        /**
         * Find teaching(교직) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun find(year: Int, semester: String): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.TEACHING))
            mutate(pressButton(BUTTON_SEARCH_TEACHING))
            parse(TimeTableParser)
        }

    }

    inner class ExtendedCollage {

        /**
         * Find extended collage(평생교육사) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun find(year: Int, semester: String): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.EXTENDED_COLLAGE))
            mutate(pressButton(BUTTON_SEARCH_EXTENDED_COLLAGE))
            parse(TimeTableParser)
        }

    }

    inner class StandardSelection {

        /**
         * Find standard selection lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun find(year: Int, semester: String): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.STANDARD_SELECTION))
            mutate(pressButton(BUTTON_SEARCH_STANDARD_SELECTION))
            parse(TimeTableParser)
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
        ): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            findOptionAndSelect(COMBOBOX_GRADUATED_SCHOOL, school)
            findOptionAndSearch(COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL, department, BUTTON_SEARCH_GRADUATED_SCHOOL)
            parse(TimeTableParser)
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
        ): Map<GraduatedMajorData, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<GraduatedMajorData, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            findOptionAndEach(COMBOBOX_GRADUATED_SCHOOL) { school, schoolKey ->
                mutate(selectOption(COMBOBOX_GRADUATED_SCHOOL, schoolKey))
                findOptionAndEach(COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL) { department, departmentKey ->
                    mutate(
                        pressButtonWithOption(
                            COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL,
                            departmentKey,
                            BUTTON_SEARCH_GRADUATED_SCHOOL
                        )
                    )
                    ret[GraduatedMajorData(school, department)] = parse(TimeTableParser)
                }
            }
            ret
        }

        /**
         * Gets a list of graduated schools.
         * @return Collection of graduated school names in String.
         */
        suspend fun getGraduatedSchools(): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            parse(ComboBoxParser(COMBOBOX_GRADUATED_SCHOOL)).keys
        }

        /**
         * Gets a list of departments in given graduated school.
         * @return Collection of departments in String.
         */
        suspend fun getDepartments(school: String): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.GRADUATED_SCHOOL))
            findOptionAndSelect(COMBOBOX_GRADUATED_SCHOOL, school)
            parse(ComboBoxParser(COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL)).keys
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
        suspend fun find(year: Int, semester: String, major: String): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.LINKED_MAJOR))
            findOptionAndSearch(COMBOBOX_LINKED_MAJOR, major, BUTTON_SEARCH_LINKED_MAJOR)
            parse(TimeTableParser)
        }

        /**
         * Find all linked major(연계전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LinkedMajorName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.LINKED_MAJOR))
            findOptionAndEach(COMBOBOX_LINKED_MAJOR) { linkedMajor, linkedMajorKey ->
                mutate(pressButtonWithOption(COMBOBOX_LINKED_MAJOR, linkedMajorKey, BUTTON_SEARCH_LINKED_MAJOR))
                ret[linkedMajor] = parse(TimeTableParser)
            }
            ret
        }

        /**
         * Gets a list of available linked majors.
         * @return Collection of major names in String.
         */
        suspend fun getLinkedMajor(): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.LINKED_MAJOR))
            parse(ComboBoxParser(COMBOBOX_LINKED_MAJOR)).keys
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
        suspend fun find(year: Int, semester: String, major: String): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.COMBINED_MAJOR))
            findOptionAndSearch(COMBOBOX_COMBINED_MAJOR, major, BUTTON_SEARCH_COMBINED_MAJOR)
            parse(TimeTableParser)
        }

        /**
         * Find all combined major(융합전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of CombinedMajorName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.COMBINED_MAJOR))
            findOptionAndEach(COMBOBOX_COMBINED_MAJOR) { combinedMajor, combinedMajorKey ->
                mutate(pressButtonWithOption(COMBOBOX_COMBINED_MAJOR, combinedMajorKey, BUTTON_SEARCH_COMBINED_MAJOR))
                ret[combinedMajor] = parse(TimeTableParser)
            }
            ret
        }

        /**
         * Gets a list of available combined majors.
         * @return Collection of major names in String.
         */
        suspend fun getCombinedMajor(): Collection<String> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            mutate(initialLoad())
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.COMBINED_MAJOR))
            parse(ComboBoxParser(COMBOBOX_COMBINED_MAJOR)).keys
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
        suspend fun find(year: Int, semester: String, professorName: String): Collection<LectureData> =
            stateSupplier().run {
                mutate(TimeTablePageNavigateAction)
                initialSemesterSelection(year, semester)
                mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.FIND_BY_PROFESSOR_NAME))
                mutate(
                    pressButtonWithChange(
                        COMBOBOX_FIND_BY_PROFESSOR_NAME,
                        professorName,
                        BUTTON_SEARCH_FIND_BY_PROFESSOR_NAME
                    )
                )
                parse(TimeTableParser)
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
        suspend fun find(year: Int, semester: String, lecture: String): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.FIND_BY_LECTURE))
            pressButtonWithChange(COMBOBOX_FIND_BY_LECTURE, lecture, BUTTON_SEARCH_FIND_BY_LECTURE)
            parse(TimeTableParser)
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
        ): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.RECOGNIZED_OTHER_MAJOR))
            findOptionAndSelect(COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR, collage)
            findOptionAndSelect(COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR, department)
            if (major != null) findOptionAndSearch(
                COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR,
                major,
                BUTTON_SEARCH_RECOGNIZED_OTHER_MAJOR
            )
            else mutate(pressButton(BUTTON_SEARCH_RECOGNIZED_OTHER_MAJOR))
            parse(TimeTableParser)
        }

        /**
         * Find recognized lectures of all majors.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of [xyz.eatsteak.kusaint.model.MajorData] paired with collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
        suspend fun all(year: Int, semester: String): Map<MajorData, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<MajorData, Collection<LectureData>>()
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.RECOGNIZED_OTHER_MAJOR))
            findOptionAndEach(COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR) { collage, collageKey ->
                mutate(selectOption(COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR, collageKey))
                findOptionAndEach(COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR) { department, departmentKey ->
                    mutate(selectOption(COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR, departmentKey))
                    findOptionAndEach(COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR) { major, majorKey ->
                        mutate(
                            pressButtonWithOption(
                                COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR,
                                majorKey,
                                BUTTON_SEARCH_RECOGNIZED_OTHER_MAJOR
                            )
                        )
                        ret[MajorData(collage, department, major)] = TimeTableParser.parse(this)
                    }
                }
            }
            ret
        }
    }

    inner class DualListing {

        /**
         * Find dual listing lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun find(year: Int, semester: String): Collection<LectureData> = stateSupplier().run {
            mutate(TimeTablePageNavigateAction)
            initialSemesterSelection(year, semester)
            mutate(selectTab(TAB_ID, PageConstant.TimeTable.TabItem.DUAL_LISTING))
            mutate(pressButton(BUTTON_SEARCH_DUAL_LISTING))
            parse(TimeTableParser)
        }
    }

    private suspend inline fun State<String>.findOptionAndSearch(comboBoxId: String, option: String, buttonId: String) {
        val options = ComboBoxParser(comboBoxId).parse(this)
        mutate(
            pressButtonWithOption(
                comboBoxId,
                options[option] ?: throw IllegalArgumentException("Cannot find option. possible values: $options"),
                buttonId
            )
        )
    }

    private suspend inline fun State<String>.findOptionAndSelect(id: String, option: String) {
        val options = ComboBoxParser(id).parse(this)
        mutate(
            selectOption(
                id,
                options[option] ?: throw IllegalArgumentException("Cannot find option. possible values: $options")
            )
        )
    }

    private suspend inline fun State<String>.findOptionAndEach(
        id: String,
        block: (option: String, optionKey: String) -> Unit
    ) {
        val options = ComboBoxParser(id).parse(this)
        options.forEach { block(it.key, it.value) }
    }

    private suspend fun State<String>.initialSemesterSelection(year: Int, semester: String) {
        mutate(initialLoad())
        mutate(selectOption(PageConstant.TimeTable.COMBOBOX_YEAR, "$year"))
        findOptionAndSelect(PageConstant.TimeTable.COMBOBOX_SEMESTER, semester)
        mutate(selectOption(PageConstant.Common.COMBOBOX_LINENUMBER, LineConstant.FIVE_HUNDRED.value))
    }
}