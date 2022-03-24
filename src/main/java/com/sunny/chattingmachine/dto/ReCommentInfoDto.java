package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReCommentInfoDto {

    private Long post_pk;
    private Long parent_pk;
    private Long reComment_pk;
    private String content;
    private boolean isRemoved;
    private AccountInfoDto writerDto;
    private final static String DEFAULT_DELETE_MESSAGE = "Deleted comment.";

    public ReCommentInfoDto(Comment reComment) {
        this.post_pk = reComment.getPost().getPostPk();
        this.parent_pk = reComment.getParent().getCommentPk();
        this.reComment_pk = reComment.getCommentPk();
        this.content = reComment.isRemoved() ? DEFAULT_DELETE_MESSAGE : reComment.getContent();
        this.isRemoved = reComment.isRemoved();
        this.writerDto = new AccountInfoDto(reComment.getWriter());

    }
}
