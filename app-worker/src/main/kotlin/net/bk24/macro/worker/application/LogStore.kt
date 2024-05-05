package net.bk24.macro.worker.application

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object LogStore {
    private val _logFlow = MutableSharedFlow<Int>() // 내부적으로 사용할 MutableSharedFlow
    val logFlow = _logFlow.asSharedFlow() // 외부에 공개할 불변 Flow

    private var counter = 0 // 로그 이벤트 카운터

    suspend fun record() {
        counter += 1 // 이벤트 카운터 증가
        _logFlow.emit(counter) // 증가된 카운터 값을 방출
    }

    fun initialize() {
        counter = 0
    }
}
