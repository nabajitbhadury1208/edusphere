package com.cts.edusphere.common.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();
    void deleteAllFiles();

    String uploadFile(MultipartFile file, String subFolder);

    Stream<Path> loadAllFiles();
    Path loadFile(String filename);

    Resource loadAsResource(String filename);

    void deleteFile(String filename);

    boolean exists(String filename);
}
