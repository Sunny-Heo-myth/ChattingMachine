package com.sunny.chattingmachine.service;

import com.sunny.chattingmachine.exception.FileException;
import com.sunny.chattingmachine.exception.FileExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public String save(MultipartFile multipartFile) {

        int extensionIdx = multipartFile.getOriginalFilename().lastIndexOf(".");
        String extension = multipartFile.getOriginalFilename().substring(extensionIdx + 1);
        String filePath = fileDir + UUID.randomUUID() + "." + extension;

        try {
            multipartFile.transferTo(new File(filePath));
        }
        catch (IOException e) {
            throw new FileException(FileExceptionType.FILE_CANNOT_SAVE);
        }
        return filePath;
    }

    @Override
    public void delete(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }
        if (!file.delete()) {
            throw new FileException(FileExceptionType.FILE_CANNOT_DELETE);
        }
    }
}
