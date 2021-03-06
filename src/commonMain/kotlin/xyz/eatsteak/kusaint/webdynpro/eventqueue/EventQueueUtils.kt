package xyz.eatsteak.kusaint.webdynpro.eventqueue

const val EVENT_SPECTATOR = "~E001"
const val EVENT_DATA_START = "~E002"
const val EVENT_DATA_END = "~E003"
const val EVENT_DATA_COLON = "~E004"
const val EVENT_DATA_COMMA = "~E005"


fun String.toEventQueueString(): String = this.fold(StringBuilder()) { builder, char ->
    if ((char in '0'..'9') || (char in 'a'..'z' || char in 'A'..'Z') || char == '-' || char == '.' || char == '_' || char == '~') {
        builder.append(char)
    } else {
        builder.append("~${char.code.toString(16).uppercase().padStart(4, '0')}")
    }
}.toString()

fun String.fromEventQueueString(): String = Regex("~([A-z0-9]{4})").replace(this) {
    it.value.replace("~", "").toInt(16).toChar().toString()
}