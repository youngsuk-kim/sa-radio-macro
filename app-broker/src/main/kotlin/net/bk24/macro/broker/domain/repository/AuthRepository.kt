package net.bk24.macro.broker.domain.repository

import net.bk24.macro.broker.domain.domain.Auth

interface AuthRepository {
    fun findAll(): MutableList<Auth>
    fun findSnsIdNotNull(): List<Auth>
    fun save(auth: Auth)
    fun findAllActiveCount(): Int
}
