package net.bk24.macro.broker.domain.repository

import net.bk24.macro.broker.domain.domain.Auth
import net.bk24.macro.broker.infrastructure.hibernate.AuthJpaRepository
import org.springframework.stereotype.Repository

@Repository
class AuthRepositoryAdapter(
    private val authJpaRepository: AuthJpaRepository,
) : AuthRepository {
    override fun findAll(): MutableList<Auth> {
        return authJpaRepository.findAll()
    }

    override fun findSnsIdNotNull(): List<Auth> {
        return authJpaRepository.findSaAuthBySnsIdNotNull()
    }

    override fun save(auth: Auth) {
        authJpaRepository.saveAndFlush(auth)
    }

    override fun findAllActiveCount(): Int {
        return authJpaRepository.findAll().count { !it.isBlocked }
    }
}
