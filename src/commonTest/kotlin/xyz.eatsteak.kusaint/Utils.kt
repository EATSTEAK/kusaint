package xyz.eatsteak.kusaint

import kotlin.coroutines.CoroutineContext

expect fun runBlockingTest(block: suspend () -> Unit)
expect val testCoroutineContext: CoroutineContext