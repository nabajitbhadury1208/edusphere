package com.cts.edusphere.services.workLoad;

import com.cts.edusphere.common.dto.workload.WorkLoadRequestDto;
import com.cts.edusphere.common.dto.workload.WorkLoadResponseDto;

import java.util.List;
import java.util.UUID;

public interface WorkLoadService {

    WorkLoadResponseDto createWorkLoad(WorkLoadRequestDto request);
    List<WorkLoadResponseDto> getAllWorkLoads();
    WorkLoadResponseDto getWorkLoadById(UUID id);
    List<WorkLoadResponseDto> getWorkLoadsByFaculty(UUID facultyId);
    WorkLoadResponseDto updateWorkLoad(UUID id, WorkLoadRequestDto request);
    void deleteWorkLoad(UUID id);
}
