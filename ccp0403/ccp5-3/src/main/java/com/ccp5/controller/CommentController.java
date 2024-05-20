package com.ccp5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp5.dto.CommentDTO;
import com.ccp5.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        if (!comments.isEmpty()) {
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@ModelAttribute CommentDTO comment) {
        commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/{cnum}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long cnum, @RequestBody CommentDTO updatedComment) {
        CommentDTO comment = commentService.getComment(cnum);
        if (comment != null) {
            commentService.updateComment(cnum, updatedComment);
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cnum}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long cnum) {
        CommentDTO comment = commentService.getComment(cnum);
        if (comment != null) {
            commentService.deleteComment(cnum);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}