package net.bk24.macro.worker.application

interface CreatePost {
    fun execute(authCode: String, index: Int)
}
