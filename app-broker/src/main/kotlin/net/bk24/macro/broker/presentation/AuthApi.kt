package net.bk24.macro.broker.presentation

import net.bk24.macro.broker.application.auth.AuthService
import net.bk24.macro.broker.presentation.dto.AuthResponse
import net.bk24.macro.broker.presentation.dto.AuthResponse.Companion.toResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthApi(
    private val authService: AuthService,
) {
    @GetMapping("/api/sa/auth")
    fun auth(): List<AuthResponse> {
        return authService.findAll().map { saAuth ->
            saAuth.toResponse()
        }.distinct()
    }

    @GetMapping("/api/auth/count")
    fun activeAuthCount(): Int {
        return authService.findAllActiveAuthCount()
    }
}
