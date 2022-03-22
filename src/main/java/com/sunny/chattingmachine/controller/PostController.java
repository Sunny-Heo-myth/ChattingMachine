package com.sunny.chattingmachine.controller;

import com.sunny.chattingmachine.domain.PostSearchCondition;
import com.sunny.chattingmachine.dto.PostSaveDto;
import com.sunny.chattingmachine.dto.PostUpdateDto;
import com.sunny.chattingmachine.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @ModelAttribute PostSaveDto postSaveDto) {
        postService.save(postSaveDto);
    }

    @PutMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("postId") Long post_pk, @ModelAttribute PostUpdateDto postUpdateDto) {
        postService.update(post_pk, postUpdateDto);
    }

    @DeleteMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("postId") Long post_pk) {
        postService.delete(post_pk);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity getInfo(@PathVariable("postId") Long post_pk) {
        return ResponseEntity.ok(postService.getPostInfo(post_pk));
    }

    @GetMapping("/post")
    public ResponseEntity search(Pageable pageable, @ModelAttribute PostSearchCondition postSearchCondition) {
        return ResponseEntity.ok(postService.getPostList(pageable, postSearchCondition));
    }
}
