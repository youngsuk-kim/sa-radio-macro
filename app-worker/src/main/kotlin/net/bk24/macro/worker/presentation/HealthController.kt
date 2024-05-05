package net.bk24.macro.worker.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/")
    fun healthCheck(): String? {
        return "hello hi"
    }
}
