package com.cts.edusphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Entry point for the EduSphere Spring Boot application.
 *
 * <p>EduSphere is an academic management platform that provides services for
 * managing users, students, faculty, courses, departments, exams, grades,
 * notifications, theses, research projects, and compliance records.</p>
 *
 * <p>This class bootstraps the Spring application context with the following
 * capabilities enabled:</p>
 * <ul>
 *   <li>JPA auditing for automatic population of audit fields (e.g., createdAt, updatedAt).</li>
 *   <li>AspectJ auto-proxy support for AOP-based cross-cutting concerns.</li>
 *   <li>Full Spring Boot auto-configuration via {@link SpringBootApplication}.</li>
 * </ul>
 */
@EnableJpaAuditing
@SpringBootApplication
@EnableAspectJAutoProxy
public class EduSphere {

    /**
     * Application entry point. Delegates to {@link SpringApplication#run(Class, String[])}
     * to bootstrap the Spring application context and start the embedded web server.
     *
     * @param args command-line arguments passed to the application at startup
     */
    public static void main(String[] args) {

        SpringApplication.run(EduSphere.class, args);
    }
}
