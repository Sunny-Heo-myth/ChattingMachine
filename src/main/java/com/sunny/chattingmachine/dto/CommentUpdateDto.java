package com.sunny.chattingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommentUpdateDto {

    @NotBlank
    private final Optional<String> content;
}
