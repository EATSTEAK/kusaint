package xyz.eatsteak.kusaint.webdynpro.app

import xyz.eatsteak.kusaint.webdynpro.component.*

@kotlinx.serialization.Serializable
class TimeTableApplication: WebDynProApplication("https://ecc.ssu.ac.kr", "zcmw2100", "강의시간표") {


    // Main Page(VIW_MAIN)
    val comboBoxMainYear = ComboBox("ZCMW_PERIOD_RE.ID_A61C4ED604A2BFC2A8F6C6038DE6AF18:VIW_MAIN.PERYR")
    val comboBoxMainSemester = ComboBox("ZCMW_PERIOD_RE.ID_A61C4ED604A2BFC2A8F6C6038DE6AF18:VIW_MAIN.PERID")

    val tabStripMain = TabStrip("ZCMW2100.ID_0001:VIW_MAIN.MODULE_TABSTRIP")

    val tableUieTable = Table("SALV_WD_TABLE.ID_DE0D9128A4327646C94670E2A892C99C:VIEW_TABLE.SALV_WD_UIE_TABLE-contentTBody")

    // Modules(VIW_MODULES)
    val comboBoxModulesRow = ComboBox("ZCMW2100.ID_0001:VIW_MODULES.ROWS")

    // By Major(VIW_TAB_OTHERS)
    val tabOthers = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_OTHERS", 0, 0)
    val comboBoxTabOthersCollage = ComboBox("ZCMW2100.ID_0001:VIW_TAB_OTHERS.DDK_LV3")
    val comboBoxTabOthersDepartment = ComboBox("ZCMW2100.ID_0001:VIW_TAB_OTHERS.DDK_LV4")
    val comboBoxTabOthersMajor = ComboBox("ZCMW2100.ID_0001:VIW_TAB_OTHERS.DDK_LV5")
    val buttonTabOthers = Button("ZCMW2100.ID_0001:VIW_TAB_OTHERS.BUTTON")

    // By Required Elective(VIW_TAB_GENERAL_REQ)
    val tabGeneralReq = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_GENERAL_REQ", 1, 0)
    val comboBoxTabGeneralReqRequiredElective = ComboBox("ZCMW2100.ID_0001:VIW_TAB_GENERAL_REQ.SM_OBJID")
    val buttonTabGeneralReqSearch = Button("ZCMW2100.ID_0001:VIW_TAB_GENERAL_REQ.BUTTON_SEARCH")

    // By Optional Elective(VIW_TAB_GENERAL_OPT)
    val tabGeneralOpt = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_GENERAL_OPT", 2, 0)
    val comboBoxTabGeneralOptDisciplines = ComboBox("ZCMW2100.ID_0001:VIW_TAB_GENERAL_OPT.DISCIPLINES")
    val buttonTabGeneralOptSearch = Button("ZCMW2100.ID_0001:VIW_TAB_GENERAL_OPT.BUTTON_SEARCH")

    // Chapel(VIW_TAB_CHAPEL_REQ)
    val tabChapelReq = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_CHAPEL_REQ", 3, 0)
    val comboBoxTabChapelReqChapel = ComboBox("ZCMW2100.ID_0001:VIW_TAB_CHAPEL_REQ.SM_OBJID")
    val buttonTabChapelReqSearch = Button("ZCMW2100.ID_0001:VIW_TAB_CHAPEL_REQ.BUTTON_SEARCH")

    // Edu(VIW_TAB_EDU)
    val tabEdu = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_EDU", 4, 0)
    val buttonTabEdu = Button("ZCMW2100.ID_0001:VIW_MAIN.BUTTON_EDU")

    // Lifelong Education(VIW_TAB_LIFELONG)
    val tabLifelong = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_LIFELONG", 5, 0)
    val buttonTabLifelong = Button("ZCMW2100.ID_0001:VIW_MAIN.BUTTON_LIFELONG")

    // Standard Selection(VIW_TAB_ROTC_CYBER)
    val tabStandard = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_ROTC_CYBER", 6, 0)
    val buttonTabStandardSelection = Button("ZCMW2100.ID_0001:VIW_MAIN.BUTTON_ROTC_CYBER")

    // Graduate School(VIW_TAB_GRADUATE)
    val tabGraduate = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_GRADUATE", 7, 0)
    val comboBoxTabGraduateSchool = ComboBox("ZCMW2100.ID_0001:VIW_TAB_GRADUATE.DDK_LV3")
    val comboBoxTabGraduateDepartment = ComboBox("ZCMW2100.ID_0001:VIW_TAB_GRADUATE.DDK_LV4")
    val buttonTabGraduate = Button("ZCMW2100.ID_0001:VIW_TAB_GRADUATE.BUTTON")

    // Connected Major(VIW_TAB_YOMA)
    val tabYoma = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_YOMA", 8, 0)
    val comboBoxTabYomaConnectedMajor = ComboBox("ZCMW2100.ID_0001:VIW_TAB_YOMA.CONNECT_MAJO")
    val buttonTabYomaSearch = Button("ZCMW2100.ID_0001:VIW_TAB_YOMA.BUTTON_SEARCH")

    // United Major(VIW_TAB_UNMA)
    val tabUnma = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_UNMA", 9, 0)
    val comboBoxTabUnmaUnitedMajor = ComboBox("ZCMW2100.ID_0001:VIW_TAB_UNMA.CG_OBJID")
    val buttonTabUnmaSearch = Button("ZCMW2100.ID_0001:VIW_TAB_UNMA.BUTTON_SEARCH")

    // By Professor(VIW_TAB_PROFESSOR)
    val tabProfessor = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_PROFESSOR", 10, 0)
    val comboBoxTabProfessor = ComboBox("ZCMW2100.ID_0001:VIW_TAB_PROFESSOR.PROFESSOR")
    val buttonTabProfessorSearch = Button("ZCMW2100.ID_0001:VIW_TAB_PROFESSOR.BUTTON_SEARCH")

    // By Lecture(VIW_TAB_SEARCH)
    val tabSearch = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_SEARCH", 11, 0)
    val comboBoxTabSearchText = ComboBox("ZCMW2100.ID_0001:VIW_TAB_SEARCH.SEARCH_TEXT")
    val buttonTabSearchSearch = Button("ZCMW2100.ID_0001:VIW_TAB_SEARCH.BUTTON_SEARCH")

    // Recognized other major(VIW_TAB_OTHER_GC)
    val tabOtherGc = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_OTHER_GC", 12, 0)
    val comboBoxTabOtherGcCollage = ComboBox("ZCMW2100.ID_0001:VIW_TAB_OTHER_GC.DDK_LV3")
    val comboBoxTabOtherGcDepartment = ComboBox("ZCMW2100.ID_0001:VIW_TAB_OTHER_GC.DDK_LV4")
    val comboBoxTabOtherGcMajor = ComboBox("ZCMW2100.ID_0001:VIW_TAB_OTHER_GC.DDK_LV5")
    val buttonTabOtherGc = Button("ZCMW2100.ID_0001:VIW_TAB_OTHER_GC.BTN_OTHER_GC")

    // Duallisting Lecture(VIW_TAB_DUALLT_SM)
    val tabDualltSm = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_DUALLT_SM", 13, 0)
    val buttonTabDualltSm = Button("ZCMW2100.ID_0001:VIW_TAB_DUALLT_SM.BTN_DUALLT_SM")

    // Cyber(VIW_TAB_CYBER)
    val tabCyber = TabStrip.Item("ZCMW2100.ID_0001:VIW_MAIN.TAB_CYBER", 14, 0)
    val buttonTabCyber = Button("ZCMW2100.ID_0001:VIW_MAIN.BTN_CYBER")
}