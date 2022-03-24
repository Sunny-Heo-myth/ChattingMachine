package com.sunny.chattingmachine.domain;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Table(name = "Account")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_pk")
    private Long accountPk;

    @Column(nullable = false, length = 30, unique = true)
    private String accountId;

    @Column(nullable = false, length = 30) //*
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30)
    private String nickName;

    @Column(nullable = false, length = 30)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountRole accountRole;

    @Column(length = 1000)
    private String refreshToken;

    @Builder.Default
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    // utilities

    public void addPost(Post post) {
        postList.add(post);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateAge(int age) {
        this.age = age;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword) {
        return !passwordEncoder.matches(checkPassword, getPassword());
    }

    public void addUserAuthority() {
        this.accountRole = AccountRole.USER;
    }

}
