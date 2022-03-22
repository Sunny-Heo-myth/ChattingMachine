package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.config.SecurityUtil;
import com.sunny.chattingmachine.domain.Post;
import com.sunny.chattingmachine.domain.PostSearchCondition;
import com.sunny.chattingmachine.dto.PostInfoDto;
import com.sunny.chattingmachine.dto.PostPagingDto;
import com.sunny.chattingmachine.dto.PostSaveDto;
import com.sunny.chattingmachine.dto.PostUpdateDto;
import com.sunny.chattingmachine.exception.*;
import com.sunny.chattingmachine.repository.AccountRepository;
import com.sunny.chattingmachine.repository.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.processing.FilerException;
import javax.transaction.Transactional;

import static com.sunny.chattingmachine.exception.PostExceptionType.POST_NOT_FOUND;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final FileService fileService;

    public PostServiceImpl(PostRepository postRepository, AccountRepository accountRepository, FileService fileService) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.fileService = fileService;
    }

    @Override
    public void save(PostSaveDto postSaveDto) throws FileException {
        Post post = postSaveDto.toEntity();

        post.confirmWriter(accountRepository.findByAccountId(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new AccountException(AccountExceptionType.ACCOUNT_NOT_FOUND)));

        postSaveDto.getUploadFile().ifPresent(
                file -> post.updateFilePath(fileService.save(file))
        );

        postRepository.save(post);
    }

    @Override
    public void update(Long post_pk, PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(post_pk)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        checkAuthority(post, PostExceptionType.NOT_AUTHORITY_UPDATE_POST);

        postUpdateDto.getTitle().ifPresent(post::updateTitle);
        postUpdateDto.getContent().ifPresent(post::updateContent);

        if (post.getFilePath() != null) {
            fileService.delete(post.getFilePath());
        }

        postUpdateDto.getUploadFile().ifPresentOrElse(
                multipartFile -> post.updateFilePath(fileService.save(multipartFile)),
                () -> post.updateFilePath(null)
        );
    }

    @Override
    public void delete(Long post_pk) {
        Post post = postRepository.findById(post_pk)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND)
        );

        checkAuthority(post, PostExceptionType.NOT_AUTHORITY_DELETE_POST);

        if (post.getFilePath() != null) {
            fileService.delete(post.getFilePath());
        }

        postRepository.delete(post);
    }

    private void checkAuthority(Post post, PostExceptionType postExceptionType) {
        if (!post.getWriter().getAccountId().equals(SecurityUtil.getLoginUsername())) {
            throw new PostException(postExceptionType);
        }
    }

    @Override
    public PostInfoDto getPostInfo(Long post_pk) {
        return new PostInfoDto(postRepository.findWithWriterById(post_pk)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND)));
    }

    @Override
    public PostPagingDto getPostList(Pageable pageable, PostSearchCondition postSearchCondition) {
    return new PostPagingDto(postRepository.search(postSearchCondition, pageable));

    }

}
