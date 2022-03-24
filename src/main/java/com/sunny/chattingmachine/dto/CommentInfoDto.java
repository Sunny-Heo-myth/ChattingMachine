package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CommentInfoDto {

    private final Long post_pk;
    private final Long comment_pk;
    private final String content;
    private final boolean isRemoved;
    private final AccountInfoDto writerDto;
    private final List<ReCommentInfoDto> reCommentInfoDtoList;
    private final static String DEFAULT_DELETE_MESSAGE = "Deleted comment.";

    public CommentInfoDto(Comment comment, List<Comment> reCommentList) {
        this.post_pk = comment.getPost().getPostPk();
        this.comment_pk = comment.getCommentPk();
        this.content = comment.isRemoved() ? DEFAULT_DELETE_MESSAGE : comment.getContent();
        this.isRemoved = comment.isRemoved();
        this.writerDto = new AccountInfoDto(comment.getWriter());
        this.reCommentInfoDtoList = reCommentList.stream()
                .map(ReCommentInfoDto::new).toList();
    }
}
