package xyz.eatsteak.kusaint.parser

import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.util.JSDOM

actual object SsoFormParser : Parser<String, SsoForm> {
    actual override suspend fun parse(state: State<String>): SsoForm {
        val lastMutation = state.mutations.last()
        val doc = JSDOM(lastMutation.result)
        val formElem = doc.window.document.querySelector("form[name=\"LoginInfo\"]") ?: throw IllegalStateException("Cannot find LoginInfo form, Is loaded page is sso page?")
        val inTpBitElem = formElem.querySelector("input[name=\"in_tp_bit\"]") ?: throw IllegalStateException("Cannot find in_tp_bit input, Is sso site is changed?")
        val rqstCausCdElem = formElem.querySelector("input[name=\"rqst_caus_cd\"]") ?: throw IllegalStateException("Cannot find rqst_caus_cd input, Is sso site is changed?")
        val ssoForm = SsoForm(
            inTpBit = (inTpBitElem.getAttribute("value") ?: throw IllegalStateException("Cannot find value of in_tp_bit.")) as String,
            rqstCausCd = (rqstCausCdElem.getAttribute("value") ?: throw IllegalStateException("Cannot find value of rqst_caus_cd.")) as String
        )
        return ssoForm
    }
}