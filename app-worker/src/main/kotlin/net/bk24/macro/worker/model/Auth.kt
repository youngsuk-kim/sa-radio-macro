package net.bk24.macro.worker.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "sa_auth")
class Auth(
    @Id
    var id: Long? = null,

    @Column
    var code: String,

    @Column
    var isBlocked: Boolean,

    @Column
    var snsId: String? = null,

    @Column
    var postNo: String? = null,

    @Column
    var memo: String? = null,

    @Column
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    constructor() : this(
        code = "",
        isBlocked = false,
        snsId = "",
        postNo = "",
        memo = "",
    )
}
