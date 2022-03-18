package com.sunny.chattingmachine.domain;

import com.sunny.chattingmachine.domain.account.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Table(name="Comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Account writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean isRemoved= false;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    @Builder
    public Comment(Account writer, Post post, Comment parent, String content) {
        this.writer = writer;
        this.post = post;
        this.parent = parent;
        this.content = content;
        this.isRemoved = false;
    }

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

}
