package xyz.eatsteak.kusaint.webdynpro.eventqueue

class EventQueueBuilder(block: EventQueueBuilder.() -> Unit) {

    private val builder = StringBuilder()

    init {
        block.invoke(this)
    }

    fun add(controlName: String, eventName: String, block: EventBuilder.() -> Unit) {
        val eventBuilder = EventBuilder(controlName, eventName, block)
        add(eventBuilder)
    }

    fun add(event: EventBuilder) {
        if (builder.isNotEmpty()) builder.append(EVENT_SPECTATOR)
        builder.append(event.build())
    }

    infix fun append(queue: EventQueueBuilder): EventQueueBuilder {
        builder.append(EVENT_SPECTATOR)
        builder.append(queue.builder)
        return this
    }

    fun build(): String {
        return builder.toString()
    }
}