package com.cts.edusphere.aspects;

import com.cts.edusphere.enums.AuditEntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComplianceAudit {
    AuditEntityType entityType();
    String scope() default "System auto generated audit response";
}
