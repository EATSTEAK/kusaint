package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.action.TimeTablePageNavigateAction
import xyz.eatsteak.kusaint.state.StateBuilders

object Kusaint {
    suspend fun getTimeTable() {
        val eccState = StateBuilders.ECC.apply {
            addMutations(TimeTablePageNavigateAction)
        }.build()
        eccState.mutations.forEach {
            println(it.result.status)
        }
    }
}