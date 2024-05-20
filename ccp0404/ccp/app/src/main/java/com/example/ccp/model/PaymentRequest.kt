package com.example.ccp.model

data class PaymentRequest(
    var id: Long? = null,
    var status: String? = null, // 예: "REQUESTED", "COMPLETED"
    var user: User? = null,
    var board: BoardDTO? = null
)
