package com.cts.edusphere.mappers;


import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;
import com.cts.edusphere.modules.WorkLoad;
import org.springframework.stereotype.Component;

@Component
public class WorkLoadMapper {

    public WorkLoad toEntity(WorkLoadRequestDto request) {
        return WorkLoad.builder()
                .faculty(request.facultyId())
                .course(request.courseId())
                .hours(request.hours())
                .semester(request.semester())
                .status(request.status())
                .build();
    }

    public WorkLoadResponseDto toResponse(WorkLoad workLoad) {
        return new WorkLoadResponseDto(
                workLoad.getFaculty(),
                workLoad.getCourse(),
                workLoad.getHours(),
                workLoad.getSemester(),
                workLoad.getStatus()
        );
    }

}
