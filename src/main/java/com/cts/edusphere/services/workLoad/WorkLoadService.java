package com.cts.edusphere.services.workLoad;

import com.cts.edusphere.common.dto.workload.WorkLoadRequest;
import com.cts.edusphere.common.dto.workload.WorkLoadResponse;

import java.util.List;
import java.util.UUID;

public interface WorkLoadService {

    WorkLoadResponse createWorkLoad(WorkLoadRequest request);
    List<WorkLoadResponse> getAllWorkLoads();
    WorkLoadResponse getWorkLoadById(UUID id);
    List<WorkLoadResponse> getWorkLoadsByFaculty(UUID facultyId);
    WorkLoadResponse updateWorkLoad(UUID id, WorkLoadRequest request);
    void deleteWorkLoad(UUID id);
}
