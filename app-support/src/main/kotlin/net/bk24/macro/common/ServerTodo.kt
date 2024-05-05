package net.bk24.macro.common

data class ServerTodo(
    val writerId: String = "",
    var codes: List<String> = emptyList(),
    var likeCodes: List<String> = emptyList(),
    val snsIds: List<String> = emptyList(),
    var postNos: List<Int> = emptyList(),
    val type: Type = Type.COMMENT,
    val upNo: Int = 0,
) {

    enum class Type {
        POST,
        COMMENT,
    }

    fun remix() {
        this.codes = this.codes.shuffled()
    }

    fun firstCode() = codes.first()
}
