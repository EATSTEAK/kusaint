package xyz.eatsteak.kusaint.eventqueue

const val EVENT_SPECTATOR = "~E001"
const val EVENT_DATA_START = "~E002"
const val EVENT_DATA_END = "~E003"
const val EVENT_DATA_COLON = "~E004"
const val EVENT_DATA_COMMA = "~E005"


fun String.toEventQueueString(): String = this.fold(StringBuilder()) { builder, char ->
    if ((char in '0'..'9') || (char in 'a'..'z' || char in 'A'..'Z') || char == '-' || char == '.' || char == '_') {
        builder.append(char)
    } else {
        builder.append("~${char.code.toString(16).uppercase().padStart(4, '0')}")
    }
}.toString()