package com.vrx.electronic.store.service.impl;

import com.vrx.electronic.store.exception.BadApiRequest;
import com.vrx.electronic.store.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("Filename:{}", originalFilename);
        String filename = UUID.randomUUID().toString();
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension; // we are not using original filename while saving
        String fullPathWithFileName = path + fileNameWithExtension;
        logger.info("Filepath: {}",fullPathWithFileName);
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")) {
            //file save
            File folder = new File(path);
            if (!folder.exists()) {
                //create the folder
                boolean mkdirs = folder.mkdirs();
                logger.info("Directory created: {}",mkdirs);
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new BadApiRequest("File with this extension:" + extension + " is not allowed !!");
        }
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + fileName;
        try {
            InputStream inputStream = new FileInputStream(fullPath);
            logger.info("Resources found with name: {}", fileName);
            return inputStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
