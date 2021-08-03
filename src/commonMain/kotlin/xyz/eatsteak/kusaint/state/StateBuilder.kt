package xyz.eatsteak.kusaint.state

import xyz.eatsteak.kusaint.action.Action

class StateBuilder<T>(private val baseState: State<T>) {

    private val actionQueue = mutableListOf<Action<T>>()

    fun addMutations(action: Action<T>): StateBuilder<T> {
        actionQueue.add(action)
        return this
    }

    suspend fun build(): State<T> {
        actionQueue.forEach {
            baseState.mutate(it)
        }
        return baseState
    }
}