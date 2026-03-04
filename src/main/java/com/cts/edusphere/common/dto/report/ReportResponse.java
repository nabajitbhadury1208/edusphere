package com.cts.edusphere.common.dto.report;

import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.User;

import java.util.UUID;

public record ReportResponse(
        UUID id,
        String metrics,
        Status status,
        ReportScope scope,
        Department department,
        User generatedBy
) {
}
