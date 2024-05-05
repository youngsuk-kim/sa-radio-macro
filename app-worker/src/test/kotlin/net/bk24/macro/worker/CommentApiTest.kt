package net.bk24.macro.worker

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import net.bk24.macro.worker.model.Auth
import net.bk24.macro.worker.model.AuthRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.data.relational.core.query.Update.update
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("live")
class CommentPostingServiceTest {

    @Autowired
    private lateinit var authRepository: AuthRepository

    @Autowired
    private lateinit var r2dbcEntityTemplate: R2dbcEntityTemplate

    @Test
    @Rollback(false)
    fun postCommentsWithChecks() = runBlocking {
        // Retrieve all users that are not blocked
        val users = authRepository.findAll().filter { !it.isBlocked }.collectList().awaitSingle()

        // Iterate over users and post comments
        users.forEach { user ->

        }
    }

    private fun blockUser(user: Auth, responseBody: String) {
        user.isBlocked = true
        user.memo = responseBody
        r2dbcEntityTemplate.update(Auth::class.java)
            .matching(query(where("code").`is`(user.code)))
            .apply(update("isBlocked", true))
            .block()

        r2dbcEntityTemplate.update(Auth::class.java)
            .matching(query(where("code").`is`(user.code)))
            .apply(update("memo", user.memo))
            .block()
    }
}
