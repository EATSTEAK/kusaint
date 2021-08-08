package xyz.eatsteak.kusaint

import kotlin.test.Test
import kotlin.test.assertEquals

class TimeTableTests {

    @Test
    fun retrieveOneMajorTest() = runBlockingTest {
        val timeTableData = Kusaint().timeTable.Major().find(2021, "2 학기", "IT대학", "글로벌미디어학부")
        assertEquals(true, timeTableData.isNotEmpty())
        println(timeTableData)
    }

    @Test
    fun retrieveAllMajorsTest() = runBlockingTest {
        val timeTableData = Kusaint().timeTable.Major().all(2021, "2 학기")
        timeTableData.entries.forEach {
            assertEquals(true, it.value.isNotEmpty())
        }
        println(timeTableData.keys)
    }

    @Test
    fun retrieveOneRequiredElectiveTest() = runBlockingTest {
        val timeTableData = Kusaint().timeTable.RequiredElective().find(2021, "2 학기", "컴퓨팅적사고")
        assertEquals(true, timeTableData.isNotEmpty())
        println(timeTableData)
    }

    @Test
    fun retrieveAllRequiredElectivesTest() = runBlockingTest {
        val timeTableData = Kusaint().timeTable.RequiredElective().all(2021, "2 학기")
        timeTableData.entries.forEach {
            assertEquals(true, it.value.isNotEmpty())
        }
        println(timeTableData.keys)
    }

    @Test
    fun retrieveOneOptionalElectiveTest() = runBlockingTest {
        val timeTableData = Kusaint().timeTable.OptionalElective().find(2021, "2 학기", "['20이후]창의/융합역량-자연과학/공학/기술")
        assertEquals(true, timeTableData.isNotEmpty())
        println(timeTableData)
    }

    @Test
    fun retrieveAllOptionalElectivesTest() = runBlockingTest {
        val timeTableData = Kusaint().timeTable.OptionalElective().all(2021, "2 학기")
        timeTableData.entries.forEach {
            assertEquals(true, it.value.isNotEmpty())
        }
        println(timeTableData.keys)
    }
}