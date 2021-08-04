package xyz.eatsteak.kusaint.util

import org.jsoup.Jsoup
import org.jsoup.parser.Parser

actual fun updatePage(original: String, updateResponse: String): String {
    val originalDoc = Jsoup.parse(original)
    val updateDoc = Jsoup.parse(updateResponse, "", Parser.xmlParser())
    println("[INFO] Getting nodes from Document.")
    val updateData = updateDoc.getElementsByTag("updates").first()?.children()?.first()
    println("[INFO] Executing update strategy ${updateData?.tagName()}")
    when(updateData?.tagName()) {
        "full-update" -> {
            val windowId = updateData.attr("windowId")
            val window = originalDoc.select("[id=\"$windowId\"]").first()
            if(window != null) {
                println("[INFO] Executing full-update on window $windowId")
                updateData.children().forEach {
                    when(it.tagName()) {
                        "content-update" -> {
                            val contentId = it.id()
                            val target = window.select("[id=\"$contentId\"]").first()
                            val content = it.getElementsByTag("content").first()
                            if(target != null && content != null && target.parent() != null) {
                                println("[INFO] Executing control-update on content $contentId")
                                target.parent()!!.html(content.html().replace("<![CDATA[", "").replace("]]>", ""))
                            }
                        }
                    }
                }
            }
        }
        "delta-update" -> {
            val windowId = updateData.attr("windowId")
            val window = originalDoc.select("[id=\"$windowId\"]").first()
            if(window != null) {
                println("[INFO] Executing delta-update on window $windowId")
                updateData.children().forEach {
                    when(it.tagName()) {
                        "control-update" -> {
                            val controlId = it.id()
                            val target = window.select("[id=\"$controlId\"]").first()
                            val content = it.getElementsByTag("content").first()
                            if(target != null && content != null && target.parent() != null) {
                                println("[INFO] Executing control-update on control $controlId")
                                target.parent()!!.html(content.html().replace("<![CDATA[", "").replace("]]>", ""))
                            }
                        }
                    }
                }
            }
        }
        else -> return originalDoc.toString()
    }
    return originalDoc.toString()
}