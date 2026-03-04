package com.cts.edusphere.common.dto.report;

import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportRequest(

        @NotBlank
        @Size(min = 3, max = 200, message = "Metrics must be between 3 and 200 characters")
        String metrics,

        @NotNull(message = "Status is required")
        Status status,

        @NotNull(message = "Scope is required")
        ReportScope scope,

        @NotNull(message = "Department is required")
        Department department,

        @NotNull
        User generatedBy
) {
}
