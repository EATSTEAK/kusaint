package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.state.State

actual class ComboBoxParser actual constructor(private val comboBoxId: String) : Parser<String, Map<String, String>> {
    actual override suspend fun parse(state: State<String>): Map<String, String> {
        TODO("Not yet implemented")
    }

}