package xyz.eatsteak.kusaint.action

class ActionResult<T>(val action: Action<T>, val result: T, val beforeMutations: List<ActionResult<*>>)