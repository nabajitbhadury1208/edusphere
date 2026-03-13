package com.cts.edusphere.common.dto.report;

import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.user.User;

import java.util.UUID;

public record ReportResponseDto(
        UUID id,
        String metrics,
        Status status,
        ReportScope scope,
        UUID department,
        User generatedBy
) {
}
