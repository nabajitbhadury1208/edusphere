package com.cts.edusphere.mappers;


import com.cts.edusphere.common.dto.workload.WorkLoadRequest;
import com.cts.edusphere.common.dto.workload.WorkLoadResponse;
import com.cts.edusphere.modules.WorkLoad;
import org.springframework.stereotype.Component;

@Component
public class WorkLoadMapper {

    public WorkLoad toEntity(WorkLoadRequest request) {
        return WorkLoad.builder()
                .faculty(request.faculty())
                .course(request.course())
                .hours(request.hours())
                .semester(request.semester())
                .status(request.status())
                .build();
    }

    public WorkLoadResponse toResponse(WorkLoad workLoad) {
        return new WorkLoadResponse(
                workLoad.getFaculty(),
                workLoad.getCourse(),
                workLoad.getHours(),
                workLoad.getSemester(),
                workLoad.getStatus()
        );
    }

}
