package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.Comment;
import com.sunny.chattingmachine.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostInfoDto {

    private final Long post_pk;
    private final String title;
    private final String content;
    private final String filePath;
    private final AccountInfoDto writerDto;
    private final List<CommentInfoDto> commentInfoDtoList;

    public PostInfoDto(Post post) {
        this.post_pk = post.getPost_pk();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.filePath = post.getFilePath();
        this.writerDto = new AccountInfoDto(post.getWriter());

        Map<Comment, List<Comment>> commentListMap = post.getCommentList().stream()
                .filter(comment -> comment.getParent() != null)
                .collect(Collectors.groupingBy(Comment::getParent));

        this.commentInfoDtoList = commentListMap.keySet().stream()
                .map(comment -> new CommentInfoDto(comment, commentListMap.get(comment)))
                .toList();
    }
}
