package com.cts.edusphere.common.dto.report;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.ReportScope;
import com.cts.edusphere.enums.Status;
import com.cts.edusphere.modules.Department;
import com.cts.edusphere.modules.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ReportRequestDto(

        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        @Size(min = 3, max = 200, message = "Metrics must be between 3 and 200 characters")
        String metrics,

        @NotNull(groups = OnCreate.class,message = "Status is required")
        Status status,

        @NotNull(groups = OnCreate.class,message = "Scope is required")
        ReportScope scope,

        @NotNull(groups = OnCreate.class,message = "Department is required")
        UUID departmentId,

        @NotNull(groups = OnCreate.class)
        User generatedBy
) {
}
