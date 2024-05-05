package net.bk24.macro.broker.presentation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import net.bk24.macro.broker.domain.domain.Auth
import java.time.LocalDateTime

data class AuthResponse(
    @JsonProperty("id")
    val id: Long?,
    @JsonProperty("code")
    val code: String,
    @JsonProperty("is_blocked")
    val isBlocked: Boolean,
    @JsonProperty("sns_id")
    val snsId: String?,
    @JsonProperty("memo")
    val memo: String?,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime,
    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun Auth.toResponse() =
            AuthResponse(
                id = this.id,
                code = this.code,
                isBlocked = this.isBlocked,
                snsId = this.snsId,
                memo = this.memo,
                createdAt = this.createdAt,
                updatedAt = this.updatedAt,
            )
    }
}
