package net.bk24.macro.common

import io.github.serpro69.kfaker.faker

object RandomStringGenerator {
    fun execute(): String {
        val emojis = listOf(
            "\uD83D\uDE00", // 😀
            "\uD83D\uDE01", // 😁
            "\uD83D\uDE02", // 😂
            "\uD83D\uDE03", // 😃
            "\uD83D\uDE04", // 😄
            "\uD83D\uDE05", // 😅
            "\uD83D\uDE06", // 😆
            "\uD83D\uDE07", // 😇
            "\uD83D\uDE08", // 😈
            "\uD83D\uDE09", // 😉
            // Add more emojis as needed
        )

        val englishChars = ('ㄱ'..'ㅎ').toList()

        val combinedChars = (emojis + englishChars).shuffled()
        val faker = faker { }
//        return faker.random.nextUUID()
        return combinedChars.joinToString(separator = "")
    }
}
