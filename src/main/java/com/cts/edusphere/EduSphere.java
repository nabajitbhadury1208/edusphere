package com.cts.edusphere;

import com.cts.edusphere.entities.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EduSphere {

	public static void main(String[] args) {
        SpringApplication.run(EduSphere.class, args);
	}
}
