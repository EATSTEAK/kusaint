package xyz.eatsteak.kusaint.eventqueue.model

import xyz.eatsteak.kusaint.eventqueue.EVENT_DATA_COLON
import xyz.eatsteak.kusaint.eventqueue.EVENT_DATA_COMMA
import xyz.eatsteak.kusaint.eventqueue.EVENT_DATA_END
import xyz.eatsteak.kusaint.eventqueue.EVENT_DATA_START

data class UcfParameters(
    var action: UcfAction? = null,
    var enqueueCardinality: UcfCardinality? = null,
    var responseData: UcfResponseData? = null,
    var transportMethod: UcfTransportMethod? = null,
    var delay: UcfDelay? = null
) {

    constructor(block: UcfParameters.() -> Unit): this() {
        block.invoke(this)
    }

    fun build(): String = StringBuilder().apply {
        append(EVENT_DATA_START)
        val ucfParams = mutableMapOf<String, String>()
        action?.let { ucfParams["ClientAction"] = it.value }
        enqueueCardinality?.let { ucfParams["EnqueueCardinality"] = it.value }
        responseData?.let { ucfParams["ResponseData"] = it.value }
        transportMethod?.let { ucfParams["TransportMethod"] = it.value }
        delay?.let { ucfParams["Delay"] = it.value }
        ucfParams.entries.forEachIndexed { index, entry ->
            append("${entry.key}$EVENT_DATA_COLON${entry.value}")
            if (index < ucfParams.entries.size - 1) append(EVENT_DATA_COMMA)
        }
        append(EVENT_DATA_END)
    }.toString()
}

enum class UcfAction(val value: String) {
    SUBMIT("submit"),
    SUBMIT_ASYNC("submit_async"),
    ENQUEUE("enqueue"),
    NONE("none")
}

enum class UcfCardinality(val value: String) {
    MULTIPLE("multiple"),
    SINGLE("single"),
    NONE("none")
}

enum class UcfResponseData(val value: String) {
    FULL("full"),
    DELTA("delta"),
    INHERIT("inherit")
}

enum class UcfTransportMethod(val value: String) {
    FULL("full"),
    PARTIAL("partial")
}

enum class UcfDelay(val value: String) {
    FULL("full"),
    NONE("none")
}
