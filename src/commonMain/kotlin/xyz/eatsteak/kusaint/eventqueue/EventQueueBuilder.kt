package xyz.eatsteak.kusaint.eventqueue

class EventQueueBuilder(block: EventQueueBuilder.() -> Unit) {

    private val builder = StringBuilder()

    init {
        block.invoke(this)
    }

    fun addEvent(controlName: String, eventName: String, block: EventBuilder.() -> Unit) {
        val eventBuilder = EventBuilder(controlName, eventName, block)
        addEvent(eventBuilder)
    }

    fun addEvent(event: EventBuilder) {
        if (builder.isNotEmpty()) builder.append(EVENT_SPECTATOR)
        builder.append(event.build())
    }

    fun build(): String {
        return builder.toString()
    }
}