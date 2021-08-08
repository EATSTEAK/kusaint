package xyz.eatsteak.kusaint

import xyz.eatsteak.kusaint.api.RecordApi
import xyz.eatsteak.kusaint.api.TimeTableApi
import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.state.States

/**
 * Main api instance for Kusaint.
 * @param id user id for u-saint, if it isn't provided, state will use anonymous authentication.
 * @param password user password for u-saint, if it isn't provided, state will use anonymous authentication.
 * @constructor Create instance for Kusaint
 */
class Kusaint(id: String? = null, password: String? = null) {

    private val stateSupplier: suspend () -> State<String> =
        if (id == null || password == null) States.eccAnonymousState() else States.eccAuthenticatedState(id, password)

    /**
     * Returns [xyz.eatsteak.kusaint.api.TimeTableApi] instance with given authentication.
     */
    val timeTable by lazy {
        TimeTableApi(stateSupplier)
    }

    /**
     * Returns [xyz.eatsteak.kusaint.api.RecordApi] instance with given authentication.
     */
    val record by lazy {
        RecordApi(stateSupplier)
    }

}