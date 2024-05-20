package com.ccp5.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer num;
    
    @ManyToOne
    private Category category;
    
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer; // 게시글 작성자
    @Column(name = "username")
    private String username;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="regdate")
    @JsonFormat(pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private LocalDateTime regdate;
    
    private int hitcount;
    
    @Column(name = "reply_cnt")
    private Integer replyCnt;
    
    @Column(name = "totalprice")
    private int totalprice;
    
    // Image URL field
    private String imageUrl;

    // Transient field for image file
    @Transient
    private transient MultipartFile image;
    
    public Board() {
        
    }
    
    // Image URL getter
    public String getImageUrl() {
        return imageUrl;
    }

    // Image URL setter
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 이미지 필드 게터
    public MultipartFile getImage() {
        return image;
    }

    // 이미지 필드 세터
    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getHitcount() {
        return hitcount;
    }

    public void setHitcount(int hitcount) {
        this.hitcount = hitcount;
    }

    public Integer getReplyCnt() {
        return replyCnt;
    }

    public void setReplyCnt(Integer replyCnt) {
        this.replyCnt = replyCnt;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    // 작성자의 이름(writerName)을 가져오는 메서드
    public String getWriterName() {
        return writer != null ? writer.getName() : null;
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                "num=" + num +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", writer=" + writer +
                ", content='" + content + '\'' +
                ", regdate=" + regdate +
                ", hitcount=" + hitcount +
                ", replyCnt=" + replyCnt +
                ", totalprice=" + totalprice +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
