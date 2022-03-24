package com.sunny.chattingmachine.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Post")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_pk")
    private Long postPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_pk")
    private Account writer;

    @Column(length = 40, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column
    private String filePath;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // utilities

    public void confirmWriter(Account writer) {
        this.writer = writer;
        writer.addPost(this);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateFilePath(String filePath) {
        this.filePath = filePath;
    }

}
