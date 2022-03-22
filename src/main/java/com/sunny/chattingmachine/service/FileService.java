package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.exception.FileException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String save(MultipartFile multipartFile) throws FileException;

    void delete(String filePath);
}
