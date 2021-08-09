package xyz.eatsteak.kusaint

internal external fun require(name: String): dynamic
internal external val __dirname: dynamic

internal actual object PlatformInit {

    internal actual var isReady = false

    private val rootCas = require("ssl-root-cas").create()
    private val https = require("https")

    init {
        rootCas
            .addFile(__dirname + "/../ca/digicert-global-root-ca.pem")
            .addFile(__dirname + "/../ca/ssu-ac-kr.pem")
            .addFile(__dirname + "/../ca/thawte-rsa-ca.pem")
        https.globalAgent.options.ca = rootCas
        isReady = true
    }
}