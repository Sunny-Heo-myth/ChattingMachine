package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.Comment;
import com.sunny.chattingmachine.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommentSaveDto {

    @NotBlank
    private final String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
