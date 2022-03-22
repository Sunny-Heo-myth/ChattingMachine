package com.sunny.chattingmachine.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.FetchType.LAZY;

@Table(name="Comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_pk")
    private Long comment_pk;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_pk")
    private Account writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_pk")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_pk")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean isRemoved= false;

    @Builder
    public Comment(Account writer, Post post, Comment parent, String content) {
        this.writer = writer;
        this.post = post;
        this.parent = parent;
        this.content = content;
        this.isRemoved = false;
    }

    // utilities

    public void confirmWriter(Account writer) {
        this.writer = writer;
        writer.addComment(this);
    }

    public void confirmPost(Post post) {
        this.post = post;
        post.addComment(this);
    }

    public void confirmParent(Comment parent){
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Comment child){
        childList.add(child);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void remove() {
        this.isRemoved = true;
    }

    public List<Comment> findRemovableList() {
        List<Comment> result = new ArrayList<>();

        Optional.ofNullable(this.parent).ifPresentOrElse(

                parentComment -> {
                    if (parentComment.isRemoved() && parentComment.isAllChildRemoved()) {
                        result.addAll(parentComment.getChildList());
                        result.add(parentComment);
                    }
                },

                () -> {
                    if (isAllChildRemoved()) {
                        result.add(this);
                        result.addAll(this.getChildList());
                    }
                }

        );
        return result;
    }

    private boolean isAllChildRemoved() {
        return getChildList().stream()
                .map(Comment::isRemoved)
                .filter(isRemoved -> !isRemoved)
                .findAny()
                .orElse(true);
    }

}
