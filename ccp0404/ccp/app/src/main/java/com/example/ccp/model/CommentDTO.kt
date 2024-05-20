package com.example.ccp.model

data class CommentDTO(
    var cnum: Long? = null,
    var writerUsername: String? = null,
    var content: String? = null,
    var regdate: String? = null,
    var boardBnum: Int? = null
)