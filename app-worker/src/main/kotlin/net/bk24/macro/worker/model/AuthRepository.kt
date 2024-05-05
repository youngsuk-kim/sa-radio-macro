package net.bk24.macro.worker.model

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : R2dbcRepository<Auth, Long>
