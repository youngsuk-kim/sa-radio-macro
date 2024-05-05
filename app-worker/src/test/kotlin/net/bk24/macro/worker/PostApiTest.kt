package net.bk24.macro.worker

import kotlinx.coroutines.runBlocking
import net.bk24.macro.worker.application.writer.PostWriter
import org.junit.jupiter.api.Test

class PostApiTest {

    @Test
    fun post() {
        runBlocking {
            PostWriter().execute("688J993X")
        }
    }
}
