package com.cts.edusphere.common.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Service contract for file-storage operations within the EduSphere platform.
 *
 * <p>Implementations are responsible for initialising the underlying storage medium,
 * persisting uploaded files, serving them as Spring {@link Resource} objects, and
 * removing them when they are no longer needed.</p>
 */
public interface StorageService {

    /**
     * Initialises the storage backend (e.g. creates the root directory on the
     * file system) if it does not already exist.
     *
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the storage medium cannot be prepared
     */
    void init();

    /**
     * Permanently removes every file currently held in the storage backend.
     *
     * <p>Use with caution: this operation is irreversible.</p>
     */
    void deleteAllFiles();

    /**
     * Persists a multipart file upload under the given sub-folder and returns a
     * relative path that can later be used to retrieve the file.
     *
     * @param file      the multipart file received from the HTTP request; must not
     *                  be empty
     * @param subFolder the relative sub-directory within the root storage location
     *                  where the file should be stored (e.g. {@code "avatars"})
     * @return a relative path string identifying the stored file, suitable for
     *         passing to {@link #loadFile(String)} or {@link #loadAsResource(String)}
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the file is empty, has no name, would escape the root directory, or
     *         an I/O error occurs
     */
    String uploadFile(MultipartFile file, String subFolder);

    /**
     * Returns a {@link Stream} of relative {@link Path} objects representing every
     * file currently held at the root level of the storage backend.
     *
     * @return a stream of relative paths; callers are responsible for closing the
     *         stream after use
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the storage root cannot be traversed
     */
    Stream<Path> loadAllFiles();

    /**
     * Resolves the given filename against the storage root and returns the
     * corresponding absolute {@link Path}.
     *
     * @param filename the relative path or file name to resolve
     * @return the resolved {@link Path}; existence is not guaranteed
     */
    Path loadFile(String filename);

    /**
     * Loads the file identified by {@code filename} as a Spring {@link Resource}
     * that can be streamed to an HTTP client.
     *
     * @param filename the relative path or file name returned by
     *                 {@link #uploadFile(MultipartFile, String)}
     * @return a readable {@link Resource} backed by the stored file
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         the file does not exist, cannot be read, or an I/O error occurs
     */
    Resource loadAsResource(String filename);

    /**
     * Deletes the file identified by {@code filename} from the storage backend.
     * If the file does not exist, the method returns silently without error.
     *
     * @param filename the relative path or file name of the file to delete
     * @throws com.cts.edusphere.exceptions.genericexceptions.StorageException if
     *         an I/O error occurs during deletion
     */
    void deleteFile(String filename);

    /**
     * Checks whether a file identified by {@code filename} currently exists in the
     * storage backend.
     *
     * @param filename the relative path or file name to test
     * @return {@code true} if the file exists; {@code false} otherwise
     */
    boolean exists(String filename);
}
