package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.api.RecordApi
import xyz.eatsteak.kusaint.api.TimeTableApi
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.state.States

class Kusaint(id: String?, password: String?) {
    private val stateSupplier: suspend () -> State<String> = if(id == null || password == null) States.eccAnonymousState() else States.eccAuthenticatedState(id, password)

    val timeTable by lazy {
        TimeTableApi(stateSupplier)
    }

    val record by lazy {
        RecordApi(stateSupplier)
    }

}