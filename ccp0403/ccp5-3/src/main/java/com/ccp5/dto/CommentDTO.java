package com.ccp5.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity (name="tbl_comment4")
public class CommentDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cnum;
    
    @ManyToOne
    @JoinColumn(name="userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer;
    
    private String content;
    
    @CreationTimestamp
    @Column(name="regdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regdate;
    
    @ManyToOne
    @JoinColumn(name ="bnum")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    public Long getCnum() {
        return cnum;
    }

    public void setCnum(Long cnum) {
        this.cnum = cnum;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getRegdate() {
        return regdate;
    }

    public void setRegdate(LocalDateTime regdate) {
        this.regdate = regdate;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }    
}