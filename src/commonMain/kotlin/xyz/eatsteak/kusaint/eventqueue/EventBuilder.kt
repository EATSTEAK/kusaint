package xyz.eatsteak.kusaint.eventqueue

class EventBuilder(private val eventName: String, block: EventBuilder.() -> Unit) {

    private val first = mutableMapOf<String, String>()
    private val second = mutableMapOf<String, String>()
    private val third = mutableMapOf<String, String>()

    init {
        block.invoke(this)
    }

    fun putFirst(key: String, value: String) = first.put(key, value)
    fun putSecond(key: String, value: String) = second.put(key, value)
    fun putThird(key: String, value: String) = third.put(key, value)

    fun build(): String = StringBuilder().apply {
        append(eventName)
        append(EVENT_DATA_START)
        first.entries.forEachIndexed { index, entry ->
            append("${entry.key}$EVENT_DATA_COLON${entry.value}")
            if (index < first.entries.size - 1) append(EVENT_DATA_COMMA)
        }
        append(EVENT_DATA_END)
        append(EVENT_DATA_START)
        second.entries.forEachIndexed { index, entry ->
            append("${entry.key}$EVENT_DATA_COLON${entry.value}")
            if (index < second.entries.size - 1) append(EVENT_DATA_COMMA)
        }
        append(EVENT_DATA_END)
        append(EVENT_DATA_START)
        third.entries.forEachIndexed { index, entry ->
            append("${entry.key}$EVENT_DATA_COLON${entry.value}")
            if (index < third.entries.size - 1) append(EVENT_DATA_COMMA)
        }
        append(EVENT_DATA_END)
    }.toString().toEventQueueString()
}