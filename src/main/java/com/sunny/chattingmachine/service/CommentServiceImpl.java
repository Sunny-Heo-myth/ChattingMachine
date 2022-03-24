package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.config.SecurityUtil;
import com.sunny.chattingmachine.domain.Comment;
import com.sunny.chattingmachine.dto.CommentSaveDto;
import com.sunny.chattingmachine.dto.CommentUpdateDto;
import com.sunny.chattingmachine.exception.*;
import com.sunny.chattingmachine.repository.AccountRepository;
import com.sunny.chattingmachine.repository.CommentRepository;
import com.sunny.chattingmachine.repository.PostRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, AccountRepository accountRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void save(Long post_pk, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmWriter(accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND)));

        comment.confirmPost(postRepository.findById(post_pk)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND)));

        commentRepository.save(comment);
    }

    @Override
    public void saveReComment(Long post_pk, Long parent_comment_pk, CommentSaveDto commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmWriter(accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND)));

        comment.confirmPost(postRepository.findById(post_pk)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND)));

        comment.confirmParent(commentRepository.findById(parent_comment_pk)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND)));

        commentRepository.save(comment);
    }

    @Override
    public void update(Long comment_pk, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(comment_pk)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        if (!comment.getWriter().getAccountId().equals(SecurityUtil.getLoginUsername())) {
            throw new CommentException(CommentExceptionType.COMMENT_NOT_AUTHORITY_UPDATE);
        }

        commentUpdateDto.getContent().ifPresent(comment::updateContent);
    }

    @Override
    public void remove(Long comment_pk) throws CommentException {
        Comment comment = commentRepository.findById(comment_pk)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        if (!comment.getWriter().getAccountId().equals(SecurityUtil.getLoginUsername())) {
            throw new CommentException(CommentExceptionType.COMMENT_NOT_AUTHORITY_DELETE);
        }

        comment.remove();
        List<Comment> removableCommentList = comment.findRemovableList();
        commentRepository.deleteAll(removableCommentList);
    }
}
