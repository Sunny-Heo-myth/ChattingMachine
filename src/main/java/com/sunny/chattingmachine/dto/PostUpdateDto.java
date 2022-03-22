package com.sunny.chattingmachine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PostUpdateDto {

    private final Optional<String> title;
    private final Optional<String> content;
    private final Optional<MultipartFile> uploadFile;

}
