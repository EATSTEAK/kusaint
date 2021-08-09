@file:JsModule("jsdom")
@file:JsNonModule
package xyz.eatsteak.kusaint.util

import kotlin.js.Promise


external class JSDOM(input: String = definedExternally, options: dynamic = definedExternally) {

    companion object {
        fun fragment(string: String = definedExternally): dynamic
        fun fromURL(url: dynamic, options: dynamic = definedExternally): Promise<JSDOM>
        fun fromFile(filename: dynamic, options: dynamic = definedExternally): JSDOM
    }
    val window: dynamic
    val virtualConsole: VirtualConsole
    val cookieJar: CookieJar
    fun serialize(): dynamic
    fun nodeLocation(node: dynamic): dynamic
    fun getInternalVMContext(): dynamic
    fun reconfigure(settings: dynamic)
    fun normalizeFromURLOptions(options: dynamic): dynamic
    fun normalizeFromFileOptions(filename: dynamic, options: dynamic): dynamic
    fun transformOptions(options: dynamic, encoding: dynamic, mimeType: dynamic): dynamic
    fun normalizeHTML(html: dynamic, mimeType: dynamic): dynamic
    fun resourcesToResourceLoader(resources: dynamic): dynamic
}


external class VirtualConsole {
    fun sendTo(anyConsole: dynamic, options: dynamic)
}

external class ResourceLoader(strictSSL: Boolean = definedExternally, proxy: dynamic = definedExternally, userAgent: dynamic = definedExternally) {
    fun _readDataURL(urlRecord: dynamic)
    fun _readFile(filePath: dynamic)
    fun fetch(urlString: String, obj: dynamic = definedExternally)
}

external class CookieJar(store: dynamic, options: dynamic) {

}