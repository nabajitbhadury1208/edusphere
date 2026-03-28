package com.cts.edusphere.common.storage;

import com.cts.edusphere.config.storage.StorageProperties;
import com.cts.edusphere.exceptions.genericexceptions.StorageException;
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

/**
 * File-system-backed implementation of {@link StorageService}.
 *
 * <p>Files are stored under a configurable root directory supplied via
 * {@link com.cts.edusphere.config.storage.StorageProperties}. Each uploaded file
 * is assigned a UUID-based name to avoid collisions, and sub-folder isolation is
 * enforced to prevent path-traversal attacks.</p>
 */
@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    /**
     * Constructs a new {@code FileSystemStorageService} using the storage location
     * defined in {@link com.cts.edusphere.config.storage.StorageProperties}.
     *
     * @param properties application storage configuration; the {@code location}
     *                   property must be non-null and non-empty
     * @throws IllegalArgumentException if the configured storage location is
     *                                  {@code null} or empty
     */
    public FileSystemStorageService(StorageProperties properties) {
        if (properties.getLocation() == null || properties.getLocation().isEmpty()) {
            throw new IllegalArgumentException("Storage location must be specified");
        }
        this.rootLocation = Path.of(properties.getLocation()).toAbsolutePath().normalize();
    }

    /**
     * Creates the root storage directory on the file system if it does not already
     * exist.
     *
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the directory cannot be created due to an I/O or security error
     */
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

    /**
     * Recursively deletes the entire root storage directory and all of its contents.
     *
     * <p>This operation is irreversible. After calling this method, {@link #init()}
     * must be called again before the service can accept new uploads.</p>
     */
    @Override
    public void deleteAllFiles() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Stores the given multipart file inside the specified sub-folder under the
     * configured root storage location.
     *
     * <p>The original file name is discarded and replaced with a randomly generated
     * {@link UUID}-based name that preserves the original file extension. The
     * sub-folder is created automatically if it does not yet exist. Path-traversal
     * attempts (i.e. destinations outside the root directory) are rejected.</p>
     *
     * @param file      the multipart file to store; must not be empty and must have
     *                  a non-blank original file name
     * @param subFolder the relative sub-directory within the root location where
     *                  the file should be placed (e.g. {@code "documents/2024"})
     * @return the path of the stored file relative to the root storage location,
     *         using the platform's default name separator
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the file is empty, has no name, the destination would escape the root
     *         directory, or an I/O error occurs during the copy
     */
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

    /**
     * Returns a {@link Stream} of {@link Path} objects for every entry at the top
     * level of the root storage directory, with paths relativised to the root.
     *
     * <p>The root directory itself is excluded from the stream. Callers should close
     * the stream after use to release the underlying file-system handle.</p>
     *
     * @return a stream of relative {@link Path} objects representing the stored files
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the root directory cannot be walked due to an I/O error
     */
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

    /**
     * Resolves the given filename or relative path against the root storage location
     * and returns the resulting {@link Path}.
     *
     * <p>This method does not verify that the resolved path actually exists.</p>
     *
     * @param filename the relative file name or path to resolve (as returned by
     *                 {@link #uploadFile(MultipartFile, String)})
     * @return the absolute {@link Path} within the root storage location
     */
    @Override
    public Path loadFile(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * Loads the file identified by {@code filename} as a Spring {@link Resource}
     * that is ready to be streamed to an HTTP client.
     *
     * @param filename the relative path or file name of the file to load
     * @return a {@link org.springframework.core.io.UrlResource} backed by the file
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the file does not exist, is not readable, or an I/O error occurs
     *         while building the resource
     */
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

    /**
     * Deletes the file identified by {@code filename} from the file system.
     *
     * <p>If the file does not exist, this method completes silently without throwing
     * an exception.</p>
     *
     * @param filename the relative path or file name of the file to delete
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         an I/O or security error prevents the file from being deleted
     */
    @Override
    public void deleteFile(String filename) {
        try {
            Path file = loadFile(filename);
            Files.deleteIfExists(file);
        } catch (Exception e) {
            throw new StorageException("Could not delete file: " + filename, e);
        }
    }

    /**
     * Tests whether a file identified by {@code filename} currently exists in the
     * storage backend.
     *
     * @param filename the relative path or file name to test
     * @return {@code true} if the path exists on the file system; {@code false}
     *         otherwise
     */
    @Override
    public boolean exists(String filename) {
        return Files.exists(loadFile(filename));
    }
}
