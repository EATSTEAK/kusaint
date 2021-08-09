package xyz.eatsteak.kusaint

external fun require(name: String): dynamic
external val __dirname: dynamic

actual object PlatformInit {

    actual var isReady = false

    val rootCas = require("ssl-root-cas").create()
    val https = require("https")

    init {
        println(__dirname)
        rootCas
            .addFile(__dirname + "/../ca/digicert-global-root-ca.pem")
            .addFile(__dirname + "/../ca/ssu-ac-kr.pem")
            .addFile(__dirname + "/../ca/thawte-rsa-ca.pem")
        https.globalAgent.options.ca = rootCas
        isReady = true
    }
}