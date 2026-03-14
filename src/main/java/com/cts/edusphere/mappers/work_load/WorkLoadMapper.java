package com.cts.edusphere.mappers.work_load;


import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.modules.courses.Course;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.modules.work_load.WorkLoad;
import org.springframework.stereotype.Component;

@Component
public class WorkLoadMapper {

    public WorkLoad toEntity(WorkLoadRequestDto request) {
        return WorkLoad.builder()
                .faculty(User.builder().id(request.facultyId()).build())
                .course(Course.builder().id(request.courseId()).build())
                .hours(request.hours())
                .semester(request.semester())
                .status(request.status())
                .build();
    }

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
