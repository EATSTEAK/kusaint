package xyz.eatsteak.kusaint.webdynpro.parser

import xyz.eatsteak.kusaint.parser.Parser
import xyz.eatsteak.kusaint.webdynpro.component.Component

interface ComponentParser<T: Component, R>: Parser<String, R> {
    val component: T
}