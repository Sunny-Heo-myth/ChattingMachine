package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.domain.PostSearchCondition;
import com.sunny.chattingmachine.dto.PostInfoDto;
import com.sunny.chattingmachine.dto.PostPagingDto;
import com.sunny.chattingmachine.dto.PostSaveDto;
import com.sunny.chattingmachine.dto.PostUpdateDto;
import com.sunny.chattingmachine.exception.FileException;
import org.springframework.data.domain.Pageable;

import javax.annotation.processing.FilerException;

public interface PostService {

    void save(PostSaveDto postSaveDto) throws FileException;

    void update(Long post_pk, PostUpdateDto postUpdateDto);

    void delete(Long post_pk);

    PostInfoDto getPostInfo(Long post_pk);

    PostPagingDto getPostList(Pageable pageable, PostSearchCondition postSearchCondition);
}
