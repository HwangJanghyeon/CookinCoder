package com.example.ccp.model

data class CommentDTO(
    var cnum: Long? = null,
    var writerUsername: String? = null, // 작성자 이름 혹은 username에 따라 이름 변경
    var content: String? = null,
    var regdate: String? = null,
    var boardBnum: Int? = null,
    var username: String? = null // 서버에서 추가된 필드 반영
)
