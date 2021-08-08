package xyz.eatsteak.kusaint.api

import xyz.eatsteak.kusaint.state.State

class RecordApi(val stateSupplier: suspend () -> State<String>)