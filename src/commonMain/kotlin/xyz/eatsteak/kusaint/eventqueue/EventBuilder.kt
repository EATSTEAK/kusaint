package xyz.eatsteak.kusaint.eventqueue

import xyz.eatsteak.kusaint.eventqueue.model.UcfParameters

class EventBuilder(private val controlName: String, private val eventName: String, block: EventBuilder.() -> Unit) {

    private val params = mutableMapOf<String, String>()
    private var ucfParams = UcfParameters()
    private val customParams = mutableMapOf<String, String>()

    init {
        block.invoke(this)
    }

    fun parameter(key: String, value: String) = params.put(key, value)
    fun ucfParameter(ucfParams: UcfParameters) { this.ucfParams = ucfParams }
    fun ucfParameter(block: UcfParameters.() -> Unit) = block.invoke(ucfParams)
    fun customParameter(key: String, value: String) = customParams.put(key, value)

    fun build(): String = StringBuilder().apply {
        append("${controlName}_$eventName")
        append(EVENT_DATA_START)
        params.entries.forEachIndexed { index, entry ->
            append("${entry.key}$EVENT_DATA_COLON${entry.value}")
            if (index < params.entries.size - 1) append(EVENT_DATA_COMMA)
        }
        append(EVENT_DATA_END)
        append(ucfParams.build())
        append(EVENT_DATA_START)
        customParams.entries.forEachIndexed { index, entry ->
            append("${entry.key}$EVENT_DATA_COLON${entry.value}")
            if (index < customParams.entries.size - 1) append(EVENT_DATA_COMMA)
        }
        append(EVENT_DATA_END)
    }.toString().toEventQueueString()
}