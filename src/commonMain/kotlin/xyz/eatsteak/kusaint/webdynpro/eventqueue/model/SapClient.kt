package xyz.eatsteak.kusaint.webdynpro.eventqueue.model

data class SapClient(
    val action: String,
    val charset: String,
    val wdSecureId: String,
    val appName: String,
    val useBeacon: Boolean
)
