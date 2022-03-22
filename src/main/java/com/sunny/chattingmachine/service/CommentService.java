package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.dto.CommentSaveDto;
import com.sunny.chattingmachine.dto.CommentUpdateDto;
import com.sunny.chattingmachine.exception.CommentException;

public interface CommentService {

    void save(Long post_pk, CommentSaveDto commentSaveDto);

    void saveReComment(Long post_pk, Long parent_comment_pk, CommentSaveDto commentSaveDto);

    void update(Long comment_pk, CommentUpdateDto commentUpdateDto);

    void remove(Long comment_pk) throws CommentException;
}
