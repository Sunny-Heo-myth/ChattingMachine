package com.sunny.chattingmachine.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BriefPostInfo {

    private Long post_pk;
    private String title;
    private String content;
    private String writerName;
    private String createdDate;

    public BriefPostInfo(Post post) {
        this.post_pk = post.getPost_pk();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writerName = post.getWriter().getName();
        this.createdDate = post.getCreatedDate().toString();
    }
}
