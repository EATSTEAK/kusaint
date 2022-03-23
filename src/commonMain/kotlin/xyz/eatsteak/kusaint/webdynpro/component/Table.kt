package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.parser.TableParser

@kotlinx.serialization.Serializable
class Table(override val id: String) : Component {

    fun parser() = TableParser(this)
}