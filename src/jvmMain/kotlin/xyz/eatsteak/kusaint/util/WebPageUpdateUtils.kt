package xyz.eatsteak.kusaint.util

import org.jsoup.Jsoup
import org.jsoup.parser.Parser

actual fun updatePage(original: String, updateResponse: String): String {
    val originalDoc = Jsoup.parse(original)
    val updateDoc = Jsoup.parse(updateResponse, "", Parser.xmlParser())
    println("[INFO] Getting nodes from Document.")
    val updateData = updateDoc.getElementsByTag("updates").first()?.children()?.first()
    println(updateData?.tagName())
    when(updateData?.tagName()) {
        "full-update" -> {
            val windowId = updateData.attr("windowId")
            val window = originalDoc.select("[id=\"$windowId\"]").first()
            if(window != null) {
                println("[INFO] Executing full-update on window $windowId")
                window.html(updateData.data())
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
                            if(target != null && content != null) {
                                println("[INFO] Executing control-update on control $controlId")
                                target.html(content.data())
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