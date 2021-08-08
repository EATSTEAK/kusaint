package xyz.eatsteak.kusaint.action

import io.ktor.client.statement.*
import kotlin.reflect.KClass

class ActionResult<T>(
    val action: KClass<out Action<T>>,
    val response: HttpResponse,
    val result: T,
    val beforeMutations: List<ActionResult<*>>
)