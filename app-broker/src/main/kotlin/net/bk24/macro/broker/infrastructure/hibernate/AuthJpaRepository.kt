package net.bk24.macro.broker.infrastructure.hibernate

import net.bk24.macro.broker.domain.domain.Auth
import org.springframework.data.jpa.repository.JpaRepository

interface AuthJpaRepository : JpaRepository<Auth, Long> {
    fun findSaAuthBySnsIdNotNull(): List<Auth>
}
