package xyz.eatsteak.kusaint.webdynpro.parser

import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.webdynpro.component.Table

expect class TableParser(component: Table) : ComponentParser<Table, Collection<Map<String, String>>> {
    override suspend fun parse(state: State<String>): Collection<Map<String, String>>
}

