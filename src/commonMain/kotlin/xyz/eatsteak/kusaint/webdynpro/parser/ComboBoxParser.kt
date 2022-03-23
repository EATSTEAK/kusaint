package xyz.eatsteak.kusaint.webdynpro.parser

import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.webdynpro.component.ComboBox

expect class ComboBoxParser(component: ComboBox) : ComponentParser<ComboBox, Map<String, String>> {
    override suspend fun parse(state: State<String>): Map<String, String>
}