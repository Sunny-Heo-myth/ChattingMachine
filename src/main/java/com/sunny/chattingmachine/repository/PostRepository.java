package com.sunny.chattingmachine.repository;

import com.sunny.chattingmachine.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    @EntityGraph(attributePaths = {"writer"})   // "select p from Post p join fetch p.writer w where p.id = :id"
    Optional<Post> findWithWriterById(Long id);

    @Override
    void delete(Post entity);
}
