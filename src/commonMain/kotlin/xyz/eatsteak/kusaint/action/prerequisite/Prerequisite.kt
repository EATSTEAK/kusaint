package xyz.eatsteak.kusaint.action.prerequisite

import xyz.eatsteak.kusaint.action.Action
import xyz.eatsteak.kusaint.action.ActionResult
import kotlin.reflect.KClass

data class Prerequisite(
    val prerequisiteStrategy: PrerequisiteStrategy = PrerequisiteStrategy.EXACT,
    val actions: List<KClass<out Action<*>>>
) {
    companion object {
        val EMPTY = Prerequisite(PrerequisiteStrategy.LAST, listOf())
    }

    fun isMatched(mutations: List<ActionResult<*>>): Boolean {
        when(prerequisiteStrategy) {
            PrerequisiteStrategy.LAST -> {
                if(mutations.size - actions.size < 0) return false
                actions.forEachIndexed { index, action ->
                    if(mutations[mutations.size - actions.size + index].action::class.simpleName !=  action.simpleName) return false
                }
                return true
            }
            PrerequisiteStrategy.FIRST -> {
                if(mutations.size < actions.size) return false
                actions.forEachIndexed { index, action ->
                    if(mutations[index].action::class.simpleName !=  action.simpleName) return false
                }
                return true
            }
            PrerequisiteStrategy.EXACT -> {
                if(mutations.size != actions.size) return false
                actions.forEachIndexed { index, action ->
                    if(mutations[index].action::class.simpleName !=  action.simpleName) return false
                }
                return true
            }
            PrerequisiteStrategy.CONTAINS -> {
                return actions.all { mutations.find { result -> result.action.simpleName == it.simpleName } != null }
            }
        }
    }
}