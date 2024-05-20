package com.example.ccp.model

data class Favorite(
    var id: Long? = null,
    var user: User? = null,
    var board: BoardDTO? = null
)
