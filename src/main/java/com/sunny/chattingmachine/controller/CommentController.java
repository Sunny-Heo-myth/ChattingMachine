package com.sunny.chattingmachine.controller;

import com.sunny.chattingmachine.dto.CommentSaveDto;
import com.sunny.chattingmachine.dto.CommentUpdateDto;
import com.sunny.chattingmachine.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void commentSave(@PathVariable("postId") Long post_pk, CommentSaveDto commentSaveDto) {
        commentService.save(post_pk, commentSaveDto);
    }

    @PostMapping("/comment/{postId}/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void reCommentSave(@PathVariable("postId") Long post_pk,
                              @PathVariable("commentId") Long comment_pk, CommentSaveDto commentSaveDto) {
        commentService.saveReComment(post_pk, comment_pk, commentSaveDto);
    }

    @PutMapping("/comment/{commentId}")
    public void update(@PathVariable("commentId") Long comment_pk, CommentUpdateDto commentUpdateDto) {
        commentService.update(comment_pk, commentUpdateDto);
    }

    @DeleteMapping("/comment/{commentId}")
    public void delete(@PathVariable("commentId") Long comment_pk) {
        commentService.remove(comment_pk);
    }
}
