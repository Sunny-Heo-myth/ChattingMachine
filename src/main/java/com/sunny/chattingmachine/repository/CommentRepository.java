package com.sunny.chattingmachine.repository;

import com.sunny.chattingmachine.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
