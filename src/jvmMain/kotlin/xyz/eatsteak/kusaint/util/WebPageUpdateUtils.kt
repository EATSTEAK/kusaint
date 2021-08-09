package xyz.eatsteak.kusaint.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser

actual fun updatePage(original: String, updateResponse: String): String {
    val originalDoc = Jsoup.parse(original)
    val updateDoc = Jsoup.parse(updateResponse, "", Parser.xmlParser())
    // println("[INFO] Getting nodes from Document.")
    val updateData = updateDoc.getElementsByTag("updates").first()?.children()?.first()
    // println("[INFO] Executing update strategy ${updateData?.tagName()}")
    val replaces = mutableMapOf<String, String>()
    when (updateData?.tagName()) {
        "full-update" -> {
            // println("[INFO] Executing full-update on window $windowId")
            updateData.children().forEach {
                when (it.tagName()) {
                    "content-update" -> {
                        val contentId = it.id()
                        val target = originalDoc.select("[id=\"$contentId\"]").first()
                        if (target != null) {
                            // println("[INFO] Executing control-update on content $contentId")
                            target.html(it.html().replace("<![CDATA[", "").replace("]]>", ""))
                        } else {
                            println("[WARN] content update target is null. contentId: $contentId")
                        }
                    }
                }

            }
        }
        "delta-update" -> {
            // println("[INFO] Executing delta-update on window $windowId")
            updateData.children().forEach {
                when (it.tagName()) {
                    "control-update" -> {
                        val controlId = it.id()
                        val target = originalDoc.select("[id=\"$controlId\"]").first()
                        val content = it.getElementsByTag("content").first()
                        if (target != null && content != null) {
                            // println("[INFO] Executing control-update on control $controlId")
                            val replacedWith = content.html().replace("<![CDATA[", "").replace("]]>", "")
                            replaces[controlId] = replacedWith
                            target.replaceWith(Element("REPLACE_TARGET_$controlId"))
                            // target.html("<![CDATA[REPLACE_TARGET_$controlId]]>")
                        } else {
                            println("[WARN/updatePage] target or content for control $controlId is null. target: $target, control: $content")
                        }
                    }
                }
            }
        }
        else -> return originalDoc.toString()
    }
    var replacedDoc = originalDoc.toString()
    replaces.forEach { (k, v) ->
        replacedDoc = replacedDoc.replace("<REPLACE_TARGET_$k></REPLACE_TARGET_$k>", v)
    }
    return replacedDoc
}