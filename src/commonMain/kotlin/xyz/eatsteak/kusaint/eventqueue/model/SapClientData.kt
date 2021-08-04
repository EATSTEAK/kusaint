package xyz.eatsteak.kusaint.eventqueue.model

data class SapClientData(
    val action: String,
    val charset: String,
    val wdSecureId: String,
    val appName: String,
    val useBeacon: Boolean
)
