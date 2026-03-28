package com.cts.edusphere.config.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the EduSphere file-storage subsystem.
 *
 * <p>Values are bound from the {@code storage.*} namespace in
 * {@code application.properties} / {@code application.yml}.  The default
 * storage root is {@code C:/uploads/documents}; override it by setting
 * {@code storage.location} in your environment-specific configuration.</p>
 */
@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {
    private String location = "C:/uploads/documents";

    /**
     * Returns the absolute path to the root directory where uploaded files are stored.
     *
     * @return the configured storage root path
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the absolute path to the root directory where uploaded files should be stored.
     *
     * @param location the new storage root path; must not be {@code null} or empty
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
