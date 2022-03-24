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
    public void commentSave(@PathVariable("postId") Long postPk,
                            CommentSaveDto commentSaveDto) {
        commentService.save(postPk, commentSaveDto);
    }

    @PostMapping("/comment/{postId}/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void reCommentSave(@PathVariable("postId") Long postPk,
                              @PathVariable("commentId") Long commentPk,
                              CommentSaveDto commentSaveDto) {
        commentService.saveReComment(postPk, commentPk, commentSaveDto);
    }

    @PutMapping("/comment/{commentId}")
    public void update(@PathVariable("commentId") Long commentPk,
                       CommentUpdateDto commentUpdateDto) {
        commentService.update(commentPk, commentUpdateDto);
    }

    @DeleteMapping("/comment/{commentId}")
    public void delete(@PathVariable("commentId") Long commentPk) {
        commentService.remove(commentPk);
    }
}
