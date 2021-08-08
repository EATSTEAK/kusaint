package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.state.State

expect class ComboBoxParser(comboBoxId: String): Parser<String, Map<String, String>> {
    override suspend fun parse(state: State<String>): Map<String, String>

}