package xyz.eatsteak.kusaint.parser

import org.jsoup.Jsoup
import xyz.eatsteak.kusaint.state.State

actual object SsoFormParser : Parser<String, SsoForm> {
    actual override suspend fun parse(state: State<String>): SsoForm {
        val lastMutation = state.mutations.last()
        val doc = Jsoup.parse(lastMutation.result)
        val formElem = doc.select("form[name=\"LoginInfo\"]").first() ?: throw IllegalStateException("Cannot find LoginInfo form, Is loaded page is sso page?")
        val inTpBitElem = formElem.select("input[name=\"in_tp_bit\"]").first() ?: throw IllegalStateException("Cannot find in_tp_bit input, Is sso site is changed?")
        val rqstCausCdElem = formElem.select("input[name=\"rqst_caus_cd\"]").first() ?: throw IllegalStateException("Cannot find rqst_caus_cd input, Is sso site is changed?")
        return SsoForm(
            inTpBit = inTpBitElem.attr("value"),
            rqstCausCd = rqstCausCdElem.attr("value")
        )
    }
}