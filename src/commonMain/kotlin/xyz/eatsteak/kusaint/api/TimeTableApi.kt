@file:Suppress("unused")

package xyz.eatsteak.kusaint.api

import xyz.eatsteak.kusaint.action.WebDynProAppNavigateAction
import xyz.eatsteak.kusaint.model.asLectureData
import xyz.eatsteak.kusaint.constant.LineConstant
import xyz.eatsteak.kusaint.model.GraduatedMajorData
import xyz.eatsteak.kusaint.model.LectureData
import xyz.eatsteak.kusaint.model.MajorData
import xyz.eatsteak.kusaint.webdynpro.parser.ComboBoxParser
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.webdynpro.app.TimeTableApplication
import xyz.eatsteak.kusaint.webdynpro.component.*

/**
 * Obtain TimeTable data from u-saint.
 * **Warning: This will not work with authentication! Use anonymous state for now.**
 * @property stateSupplier state supplier for methods, use [xyz.eatsteak.kusaint.Kusaint] for easy use. If you want more advanced usage, please refer to [xyz.eatsteak.kusaint.state.States].
 */
class TimeTableApi(val app: TimeTableApplication, val stateSupplier: suspend () -> State<String>) {
    

    val major by lazy { Major() }

    val requiredElective by lazy { RequiredElective() }

    val optionalElective by lazy { OptionalElective() }

    val chapel by lazy { Chapel() }

    val teaching by lazy { Teaching() }

    val lifelongLearning by lazy { LifelongLearning() }

    val standardSelection by lazy { StandardSelection() }

    val graduatedSchool by lazy { GraduatedSchool() }

    val connectedMajor by lazy { ConnectedMajor() }

    val unitedMajor by lazy { UnitedMajor() }

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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            app.findOptionAndSelect(this, app.comboBoxTabOthersCollage, collage)
            app.findOptionAndSelect(this, app.comboBoxTabOthersDepartment, department)
            if (major != null) app.findOptionAndPress(this, app.comboBoxTabOthersMajor, major, app.buttonTabOthers)
            else mutate(app.pressButton(app.buttonTabOthers))
            parse(app.tableUieTable.parser()).map { it.asLectureData() }
        }

        /**
         * Find lectures of all majors.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of [xyz.eatsteak.kusaint.model.MajorData] paired with collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
        suspend fun all(year: Int, semester: String): Map<MajorData, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<MajorData, Collection<LectureData>>()
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            app.findOptionAndEach(this, app.comboBoxTabOthersCollage) { collage, collageKey ->
                mutate(app.selectOption(app.comboBoxTabOthersCollage, collageKey))
                app.findOptionAndEach(this, app.comboBoxTabOthersDepartment) { department, departmentKey ->
                    mutate(app.selectOption(app.comboBoxTabOthersDepartment, departmentKey))
                    app.findOptionAndEach(this, app.comboBoxTabOthersMajor) { major, majorKey ->
                        mutate(
                            app.pressButtonWithOption(
                                app.comboBoxTabOthersMajor,
                                majorKey,
                                app.buttonTabOthers
                            )
                        )
                        ret[MajorData(collage, department, major)] = app.tableUieTable.parser().parse(this).map { it.asLectureData() }
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
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            parse(app.comboBoxTabOthersCollage.parser()).keys
        }

        /**
         * Gets a list of available departments of given collage.
         * @param collage desired collage name.
         * @return Collection of department names in String.
         */
        suspend fun getDepartments(collage: String): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            app.findOptionAndSelect(this, app.comboBoxTabOthersCollage, collage)
            app.comboBoxTabOthersDepartment.parser().parse(this).keys
        }

        /**
         * Gets a list of available majors of given collage and department.
         * @param collage desired collage name.
         * @param department desired department name.
         * @return Collection of major names in String.
         */
        suspend fun getMajors(collage: String, department: String): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            app.findOptionAndSelect(this, app.comboBoxTabOthersCollage, collage)
            app.findOptionAndSelect(this, app.comboBoxTabOthersDepartment, department)
            parse(ComboBoxParser(app.comboBoxTabOthersMajor)).keys
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
                mutate(WebDynProAppNavigateAction(app))
                initialSemesterSelection(year, semester)
                mutate(
                    app.actionWithFormRequest(app.tabStripMain.select(app.tabGeneralReq))
                )
                app.findOptionAndPress(this, app.comboBoxTabGeneralReqRequiredElective, lectureName, app.buttonTabGeneralReqSearch)
                parse(app.tableUieTable.parser()).map { it.asLectureData() }
            }

        /**
         * Find all lectures of required elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LectureName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(
                app.actionWithFormRequest(app.tabStripMain.select(app.tabGeneralReq))
            )
            app.findOptionAndEach(this, app.comboBoxTabGeneralReqRequiredElective) { elective, electiveKey ->
                app.pressButtonWithOption(
                    app.comboBoxTabGeneralReqRequiredElective,
                    electiveKey,
                    app.buttonTabGeneralReqSearch
                )
                ret[elective] = parse(app.tableUieTable.parser()).map { it.asLectureData() }
            }
            ret
        }

        /**
         * Gets a list of available lecture names in required electives.
         * @return Collection of lecture names in String.
         */
        suspend fun getLectureName(): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            mutate(
                app.actionWithFormRequest(app.tabStripMain.select(app.tabGeneralReq))
            )
            parse(app.comboBoxTabGeneralReqRequiredElective.parser()).keys
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
                mutate(WebDynProAppNavigateAction(app))
                initialSemesterSelection(year, semester)
                mutate(
                    app.actionWithFormRequest(app.tabStripMain.select(app.tabGeneralOpt))
                )
                app.findOptionAndPress(this, app.comboBoxTabGeneralOptDisciplines, category, app.buttonTabGeneralOptSearch)
                parse(app.tableUieTable.parser()).map { it.asLectureData() }
            }

        /**
         * Find all lectures of optional elective category.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of Category paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(
                app.actionWithFormRequest(app.tabStripMain.select(app.tabGeneralOpt))
            )
            app.findOptionAndEach(this, app.comboBoxTabGeneralOptDisciplines) { elective, electiveKey ->
                app.pressButtonWithOption(
                    app.comboBoxTabGeneralOptDisciplines,
                    electiveKey,
                    app.buttonTabGeneralOptSearch
                )
                ret[elective] = app.tableUieTable.parser().parse(this).map { it.asLectureData() }
            }
            ret
        }

        /**
         * Gets a list of available categories in optional electives.
         * @return Collection of categories in String.
         */
        suspend fun getCategories(): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            mutate(
                app.actionWithFormRequest(app.tabStripMain.select(app.tabGeneralOpt))
            )
            parse(app.comboBoxTabGeneralOptDisciplines.parser()).keys
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
                mutate(WebDynProAppNavigateAction(app))
                initialSemesterSelection(year, semester)
                mutate(
                    app.actionWithFormRequest(app.tabStripMain.select(app.tabChapelReq))
                )
                app.findOptionAndPress(this, app.comboBoxTabChapelReqChapel, lectureName, app.buttonTabChapelReqSearch)
                app.tableUieTable.parser().parse(this).map { it.asLectureData() }
            }

        /**
         * Find all chapel lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LectureName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabChapelReq)))
            app.findOptionAndEach(this, app.comboBoxTabChapelReqChapel) { elective, electiveKey ->
                mutate(
                    app.pressButtonWithOption(
                        app.comboBoxTabChapelReqChapel,
                        electiveKey,
                        app.buttonTabChapelReqSearch
                    )
                )
                ret[elective] = app.tableUieTable.parser().parse(this).map { it.asLectureData() }
            }
            ret
        }

        /**
         * Gets a list of available lecture names in chapels.
         * @return Collection of lecture names in String.
         */
        suspend fun getLectureNames(): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            mutate(
                app.actionWithFormRequest(app.tabStripMain.select(app.tabChapelReq))
            )
            parse(ComboBoxParser(app.comboBoxTabChapelReqChapel)).keys
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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabEdu)))
            mutate(app.pressButton(app.buttonTabEdu))
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
        }

    }

    inner class LifelongLearning {

        /**
         * Find Lifelong Learning(평생교육사) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun find(year: Int, semester: String): Collection<LectureData> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabLifelong)))
            mutate(app.pressButton(app.buttonTabLifelong))
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabStandard)))
            mutate(app.pressButton(app.buttonTabStandardSelection))
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabGraduate)))
            app.findOptionAndSelect(this, app.comboBoxTabGraduateSchool, school)
            app.findOptionAndPress(this, app.comboBoxTabGraduateDepartment, department, app.buttonTabGraduate)
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabGraduate)))
            app.findOptionAndEach(this, app.comboBoxTabGraduateSchool) { school, schoolKey ->
                mutate(app.selectOption(app.comboBoxTabGraduateSchool, schoolKey))
                app.findOptionAndEach(this, app.comboBoxTabGraduateDepartment) { department, departmentKey ->
                    mutate(
                        app.pressButtonWithOption(
                            app.comboBoxTabGraduateDepartment,
                            departmentKey,
                            app.buttonTabGraduate
                        )
                    )
                    ret[GraduatedMajorData(school, department)] = app.tableUieTable.parser().parse(this).map { it.asLectureData() }
                }
            }
            ret
        }

        /**
         * Gets a list of graduated schools.
         * @return Collection of graduated school names in String.
         */
        suspend fun getGraduatedSchools(): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabGraduate)))
            parse(ComboBoxParser(app.comboBoxTabGraduateSchool)).keys
        }

        /**
         * Gets a list of departments in given graduated school.
         * @return Collection of departments in String.
         */
        suspend fun getDepartments(school: String): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabGraduate)))
            app.findOptionAndSelect(this, app.comboBoxTabGraduateSchool, school)
            parse(ComboBoxParser(app.comboBoxTabGraduateDepartment)).keys
        }
    }

    inner class ConnectedMajor {

        /**
         * Find connected major(연계전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param major desired linked major to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun find(year: Int, semester: String, major: String): Collection<LectureData> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabYoma)))
            app.findOptionAndPress(this, app.comboBoxTabYomaConnectedMajor, major, app.buttonTabYomaSearch)
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
        }

        /**
         * Find all linked major(연계전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of LinkedMajorName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabYoma)))
            app.findOptionAndEach(this, app.comboBoxTabYomaConnectedMajor) { linkedMajor, linkedMajorKey ->
                mutate(app.pressButtonWithOption(app.comboBoxTabYomaConnectedMajor, linkedMajorKey, app.buttonTabYomaSearch))
                ret[linkedMajor] = app.tableUieTable.parser().parse(this).map { it.asLectureData() }
            }
            ret
        }

        /**
         * Gets a list of available linked majors.
         * @return Collection of major names in String.
         */
        suspend fun getLinkedMajor(): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabYoma)))
            parse(app.comboBoxTabYomaConnectedMajor.parser()).keys
        }
    }

    inner class UnitedMajor {

        /**
         * Find united major(융합전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @param major desired combined major to find.
         * @return Collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun find(year: Int, semester: String, major: String): Collection<LectureData> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabUnma)))
            app.findOptionAndPress(this, app.comboBoxTabUnmaUnitedMajor, major, app.buttonTabUnmaSearch)
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
        }

        /**
         * Find all united major(융합전공) lectures.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of CombinedMajorName paired with collection of [xyz.eatsteak.kusaint.model.LectureData]
         */
        suspend fun all(year: Int, semester: String): Map<String, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<String, Collection<LectureData>>()
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabUnma)))
            app.findOptionAndEach(this, app.comboBoxTabUnmaUnitedMajor) { combinedMajor, combinedMajorKey ->
                mutate(app.pressButtonWithOption(app.comboBoxTabUnmaUnitedMajor, combinedMajorKey, app.buttonTabUnmaSearch))
                ret[combinedMajor] = app.tableUieTable.parser().parse(this).map { it.asLectureData() }
            }
            ret
        }

        /**
         * Gets a list of available combined majors.
         * @return Collection of major names in String.
         */
        suspend fun getCombinedMajor(): Collection<String> = stateSupplier().run {
            mutate(WebDynProAppNavigateAction(app))
            mutate(app.initialLoad)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabUnma)))
            parse(ComboBoxParser(app.comboBoxTabUnmaUnitedMajor)).keys
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
                mutate(WebDynProAppNavigateAction(app))
                initialSemesterSelection(year, semester)
                mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabProfessor)))
                mutate(
                    app.pressButtonWithChange(
                        app.comboBoxTabProfessor,
                        professorName,
                        app.buttonTabProfessorSearch
                    )
                )
                app.tableUieTable.parser().parse(this).map { it.asLectureData() }
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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabSearch)))
            app.pressButtonWithChange(app.comboBoxTabSearchText, lecture, app.buttonTabSearchSearch)
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabOtherGc)))
            app.findOptionAndSelect(this, app.comboBoxTabOtherGcCollage, collage)
            app.findOptionAndSelect(this, app.comboBoxTabOtherGcDepartment, department)
            if (major != null) app.findOptionAndPress(
                this,
                app.comboBoxTabOtherGcMajor,
                major,
                app.buttonTabOtherGc
            )
            else mutate(app.pressButton(app.buttonTabOtherGc))
            app.tableUieTable.parser().parse(this).map { it.asLectureData() }
        }

        /**
         * Find recognized lectures of all majors.
         * @param year desired year to find.
         * @param semester desired semester to find. this value is normally one of: ["1 학기", "여름학기", "2 학기", "겨울학기"]
         * @return Map of [xyz.eatsteak.kusaint.model.MajorData] paired with collection of [xyz.eatsteak.kusaint.model.LectureData].
         */
        suspend fun all(year: Int, semester: String): Map<MajorData, Collection<LectureData>> = stateSupplier().run {
            val ret = mutableMapOf<MajorData, Collection<LectureData>>()
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabOtherGc)))
            app.findOptionAndEach(this, app.comboBoxTabOtherGcCollage) { collage, collageKey ->
                mutate(app.selectOption(app.comboBoxTabOtherGcCollage, collageKey))
                app.findOptionAndEach(this, app.comboBoxTabOtherGcDepartment) { department, departmentKey ->
                    mutate(app.selectOption(app.comboBoxTabOtherGcDepartment, departmentKey))
                    app.findOptionAndEach(this, app.comboBoxTabOtherGcMajor) { major, majorKey ->
                        mutate(
                            app.pressButtonWithOption(
                                app.comboBoxTabOtherGcMajor,
                                majorKey,
                                app.buttonTabOtherGc
                            )
                        )
                        ret[MajorData(collage, department, major)] = app.tableUieTable.parser().parse(this).map { it.asLectureData() }
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
            mutate(WebDynProAppNavigateAction(app))
            initialSemesterSelection(year, semester)
            mutate(app.actionWithFormRequest(app.tabStripMain.select(app.tabDualltSm)))
            mutate(app.pressButton(app.buttonTabDualltSm))
            parse(app.tableUieTable.parser())
        }.map { it.asLectureData() }
    }

    private suspend fun State<String>.initialSemesterSelection(year: Int, semester: String) {
        mutate(app.initialLoad)
        mutate(app.actionWithFormRequest { add(app.comboBoxMainYear.select("$year")) })
        app.findOptionAndSelect(this, app.comboBoxMainSemester, semester)
        mutate(app.actionWithFormRequest { add(app.comboBoxModulesRow.select(LineConstant.FIVE_HUNDRED.value)) })
    }
}