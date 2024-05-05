package net.bk24.macro.worker.infrastructure.client

import com.fasterxml.jackson.annotation.JsonProperty

data class WriteResponse(
    @JsonProperty("sp_rtn")
    val code: Long,
    @JsonProperty("error_msg")
    val errorMessage: String,
    @JsonProperty("is_scalar")
    val isScalar: String,
    @JsonProperty("row_count")
    val rowCount: Int,
)
