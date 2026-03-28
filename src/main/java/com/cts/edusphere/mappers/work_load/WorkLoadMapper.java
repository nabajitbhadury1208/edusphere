package com.cts.edusphere.mappers.work_load;


import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.modules.work_load.WorkLoad;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link WorkLoad} entity objects
 * and their corresponding DTO representations ({@link WorkLoadRequestDto} and
 * {@link WorkLoadResponseDto}).
 *
 * <p>This class is a Spring-managed component and can be injected wherever workload
 * mapping is required. Associations to {@link User} (faculty) and {@link Course} are
 * established using stub entities carrying only the IDs from the request.</p>
 */
@Component
public class WorkLoadMapper {

    /**
     * Converts a {@link WorkLoadRequestDto} to a {@link WorkLoad} entity.
     *
     * <p>The faculty and course associations are set using lightweight stub entities
     * containing only the IDs provided in the request. The persistence layer is
     * responsible for resolving these to fully-loaded entities.</p>
     *
     * @param request the {@link WorkLoadRequestDto} containing workload data to map
     * @return a new {@link WorkLoad} entity built from the request data
     */
    public WorkLoad toEntity(WorkLoadRequestDto request) {
        return WorkLoad.builder()
                .faculty(User.builder().id(request.facultyId()).build())
                .course(Course.builder().id(request.courseId()).build())
                .hours(request.hours())
                .semester(request.semester())
                .status(request.status())
                .build();
    }

    /**
     * Converts a {@link WorkLoad} entity to a {@link WorkLoadResponseDto}.
     *
     * <p>The faculty ID and course ID are extracted from the associated entities
     * embedded in the workload.</p>
     *
     * @param workLoad the {@link WorkLoad} entity to convert
     * @return a {@link WorkLoadResponseDto} populated with data from the entity
     */
    public WorkLoadResponseDto toResponse(WorkLoad workLoad) {
        return new WorkLoadResponseDto(
                workLoad.getFaculty().getId(),
                workLoad.getCourse().getId(),
                workLoad.getHours(),
                workLoad.getSemester(),
                workLoad.getStatus()
        );
    }

}
