package xyz.eatsteak.kusaint.util

import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
actual fun updatePage(original: String, updateResponse: String): String {
    // println("[INFO] Updating pages...")
    val originalDoc = JSDOM(original).window.document
    @Suppress("UNUSED_VARIABLE")
    val DOMParser = JSDOM("").window.DOMParser
    val domParser = js("new DOMParser();")
    val updateDoc = domParser.parseFromString(updateResponse, "text/xml")
    val updatesElem = (updateDoc.getElementsByTagName("updates") as ItemArrayLike<dynamic>).asList().first()
    val updateData = (updatesElem?.children as ItemArrayLike<dynamic>).asList().first()
    when ((updateData?.tagName as String?)?.lowercase()) {
        "full-update" -> {
            (updateData.children as ItemArrayLike<dynamic>).asList().toList().forEach {
                when ((it.tagName as String).lowercase()) {
                    "content-update" -> {
                        val contentId = it.id as String
                        val target = originalDoc.querySelector("[id=\"$contentId\"]")
                        if (target != null) {
                            // println("[INFO/updatePage] updating $contentId...")
                            target.innerHTML = (it.innerHTML as String).replace("<![CDATA[", "").replace("]]>", "")
                        } else {
                            println("[WARN/updatePage] Content update target is null. contentId: $contentId")
                        }
                    }
                }
            }
        }
        "delta-update" -> {
            ArrayList((updateData.children as ItemArrayLike<dynamic>).asList()).forEach {
                when ((it.tagName as String).lowercase()) {
                    "control-update" -> {
                        val controlId = it.id as String
                        val target = originalDoc.querySelector("[id=\"$controlId\"]")
                        val content = (it.getElementsByTagName("content") as ItemArrayLike<dynamic>).asList().first()
                        if (target != null && content != null) {
                            // println("[INFO/updatePage] updating $controlId...")
                            target.outerHTML = (content.innerHTML as String).replace("<![CDATA[", "").replace("]]>", "")
                        } else {
                            val targetStr = target ?: "null"
                            val contentStr = content ?: "null"
                            println("[WARN/updatePage] target or content for control $controlId is null. target: $targetStr, control: $contentStr")
                        }
                    }
                }
            }
        }
        else -> println("[WARN/updatePage] Unknown tag name: ${updateData?.tagName}")
    }
    return originalDoc.documentElement?.outerHTML as String?
        ?: throw IllegalStateException("Cannot find document element.")
}