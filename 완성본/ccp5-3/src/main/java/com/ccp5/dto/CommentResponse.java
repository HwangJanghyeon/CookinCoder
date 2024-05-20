package com.ccp5.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponse {
    private Long cnum;
    private String content;
    private LocalDateTime regdate;
    private String username; // 유저의 이름

    // 생성자 추가
    public CommentResponse(Long cnum, String content, LocalDateTime regdate, String username) {
        this.cnum = cnum;
        this.content = content;
        this.regdate = regdate;
        this.username = username;
    }
}
