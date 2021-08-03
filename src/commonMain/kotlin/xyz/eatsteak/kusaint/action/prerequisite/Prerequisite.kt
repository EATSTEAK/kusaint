package xyz.eatsteak.kusaint.action.prerequisite

import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult

data class Prerequisite(
    val prerequisiteStrategy: PrerequisiteStrategy = PrerequisiteStrategy.EXACT,
    val actions: List<Action<*>>
) {
    companion object {
        val EMPTY = Prerequisite(PrerequisiteStrategy.LAST, listOf())
    }

    fun isMatched(mutations: List<ActionResult<*>>): Boolean {
        when(prerequisiteStrategy) {
            PrerequisiteStrategy.LAST -> {
                if(mutations.size - actions.size < 0) return false
                actions.forEachIndexed { index, action ->
                    if(mutations[mutations.size - actions.size + index].action::class.simpleName !=  action::class.simpleName) return false
                }
                return true
            }
            PrerequisiteStrategy.FIRST -> {
                if(mutations.size < actions.size) return false
                actions.forEachIndexed { index, action ->
                    if(mutations[index].action::class.simpleName !=  action::class.simpleName) return false
                }
                return true
            }
            PrerequisiteStrategy.EXACT -> {
                if(mutations.size != actions.size) return false
                actions.forEachIndexed { index, action ->
                    if(mutations[index].action::class.simpleName !=  action::class.simpleName) return false
                }
                return true
            }
        }
    }
}