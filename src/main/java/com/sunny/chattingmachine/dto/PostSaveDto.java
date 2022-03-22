package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PostSaveDto {

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;

    private final Optional<MultipartFile> uploadFile;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
