package com.sunny.chattingmachine.repository;

import com.sunny.chattingmachine.domain.Post;
import com.sunny.chattingmachine.domain.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Page<Post> search(PostSearchCondition postSearchCondition, Pageable pageable);
}
