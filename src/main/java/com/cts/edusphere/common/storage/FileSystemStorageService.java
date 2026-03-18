package com.cts.edusphere.common.storage;

import com.cts.edusphere.config.storage.StorageProperties;
import com.cts.edusphere.exceptions.genericexceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        if (properties.getLocation() == null || properties.getLocation().isEmpty()) {
            throw new IllegalArgumentException("Storage location must be specified");
        }
        this.rootLocation = Path.of(properties.getLocation()).toAbsolutePath().normalize();
    }

    @Override
    public void init() {
        try {
            if (!rootLocation.toFile().exists()) {
                Files.createDirectories(rootLocation);
            }
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllFiles() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public String uploadFile(MultipartFile file, String subFolder) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            String filename = file.getOriginalFilename();
            String extension = "";
            if (filename == null || filename.isEmpty()) {
                throw new StorageException("Failed to store file with no name.");
            }
            if (filename.lastIndexOf(".") > 0) {
                extension = filename.substring(filename.lastIndexOf("."));
            }

            Path targetDir = this.rootLocation.resolve(subFolder).normalize().toAbsolutePath();
            if (!targetDir.startsWith(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside root storage directory");
            }
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            String generatedFilename = UUID.randomUUID().toString() + extension;
            Path destinationFile = targetDir.resolve(generatedFilename).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(targetDir)) {
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return this.rootLocation.toAbsolutePath().relativize(destinationFile).toString();
        } catch (StorageException e){
            throw e;
        }
        catch (Exception e) {
            throw new StorageException("Failed to store file: " + e.getMessage(), e);
        }

    }

    @Override
    public Stream<Path> loadAllFiles() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (Exception e) {
            throw new StorageException("Failed to read stored files: " + e.getMessage(), e);
        }
    }

    @Override
    public Path loadFile(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = loadFile(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Path file = loadFile(filename);
            Files.deleteIfExists(file);
        } catch (Exception e) {
            throw new StorageException("Could not delete file: " + filename, e);
        }
    }

    @Override
    public boolean exists(String filename) {
        return Files.exists(loadFile(filename));
    }
}
