package xyz.eatsteak.kusaint.constant

object PageConstant {

    object Common {
        const val COMBOBOX_LINENUMBER = "WD0159"
    }

    object TimeTable {
        const val COMBOBOX_YEAR = "WD89"
        const val COMBOBOX_SEMESTER = "WDDD"
        const val COMBOBOX_COLLAGE = "WDFA"
        const val COMBOBOX_DEPARTMENT = "WD0107"
        const val COMBOBOX_MAJOR = "WD010A"
        const val BUTTON_SEARCH_MAJOR = "WD010D"

        const val COMBOBOX_REQUIRED_ELECTIVE = "WD01DC"
        const val BUTTON_SEARCH_REQUIRED_ELECTIVE = "WD01F1"

        const val COMBOBOX_OPTIONAL_ELECTIVE = "WD01DC"
        const val BUTTON_SEARCH_OPTIONAL_ELECTIVE = "WD0214"

        const val COMBOBOX_CHAPEL = "WD01DC"
        const val BUTTON_SEARCH_CHAPEL = "WD01E1"

        const val BUTTON_SEARCH_TEACHING = "WD01D0"

        const val BUTTON_SEARCH_EXTENDED_COLLAGE = "WD01D6"

        const val BUTTON_SEARCH_STANDARD_SELECTION = "WD01D0"

        const val COMBOBOX_GRADUATED_SCHOOL = "WD01D9"
        const val COMBOBOX_DEPARTMENT_GRADUATED_SCHOOL = "WD01E7"
        const val BUTTON_SEARCH_GRADUATED_SCHOOL = "WD01EA"

        const val COMBOBOX_LINKED_MAJOR = "WD01D8"
        const val BUTTON_SEARCH_LINKED_MAJOR = "WD01E4"

        const val COMBOBOX_COMBINED_MAJOR = "WD01D8"
        const val BUTTON_SEARCH_COMBINED_MAJOR = "WD020A"

        const val COMBOBOX_FIND_BY_PROFESSOR_NAME = "WD01DA"
        const val BUTTON_SEARCH_FIND_BY_PROFESSOR_NAME = "WD01DD"

        const val COMBOBOX_FIND_BY_LECTURE = "WD01D9"
        const val BUTTON_SEARCH_FIND_BY_LECTURE = "WD01DC"

        const val COMBOBOX_COLLAGE_RECOGNIZED_OTHER_MAJOR = "WD01DC"
        const val COMBOBOX_DEPARTMENT_RECOGNIZED_OTHER_MAJOR = "WD01E9"
        const val COMBOBOX_MAJOR_RECOGNIZED_OTHER_MAJOR = "WD01EC"
        const val BUTTON_SEARCH_RECOGNIZED_OTHER_MAJOR = "WD01EF"

        const val BUTTON_SEARCH_DUAL_LISTING = "WD01DA"

        const val TAB_ID = "WDEA"
        enum class TabItem(val itemId: String, val itemIndex: Int, val firstItemIndex: Int) {
            MAJOR("WDEB", 0, 0),
            REQUIRED_ELECTIVE("WD010E", 1, 0),
            OPTIONAL_ELECTIVE("WD0111", 2, 0),
            CHAPEL("WD0114", 3, 0),
            TEACHING("WD0117", 4, 0),
            EXTENDED_COLLAGE("WD011A", 5, 0),
            STANDARD_SELECTION("WD011D", 6, 0),
            GRADUATED_SCHOOL("WD0120", 7, 0),
            LINKED_MAJOR("WD0123", 8, 0),
            COMBINED_MAJOR("WD0126", 9, 0),
            FIND_BY_PROFESSOR_NAME("WD0129", 10, 0),
            FIND_BY_LECTURE("WD012C", 11, 0),
            RECOGNIZED_OTHER_MAJOR("WD012F", 12, 0),
            DUAL_LISTING("WD0132", 13, 13)
        }
    }
}