package com.cts.edusphere.common.aspect;

import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Severity;
import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.services.audit_log.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AspectConfig {

    private final AuditLogService auditLogService;

    @Pointcut("execution(* com.cts.edusphere.controllers..*.*(..))")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering: {} with args: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {
        try {
            UUID userId = getUserId();
            String action = joinPoint.getSignature().getName();
            String resource = joinPoint.getTarget().getClass().getSimpleName();

            auditLogService.logSystemEvent(
                    SystemLogType.API_ACCESS,
                    Severity.INFO,
                    action,
                    resource,
                    null,
                    userId
            );
        } catch (Exception e) {
            log.warn("Failed to write success audit log: {}", e.getMessage());
        }
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterThrow(JoinPoint joinPoint, Throwable ex) {
        try {
            UUID userId = getUserId();
            String action = joinPoint.getSignature().getName();
            String resource = joinPoint.getTarget().getClass().getSimpleName();
            String details = ex.getClass().getSimpleName() + ": " + ex.getMessage();

            log.error("Method {} on {} threw: {}", action, resource, details);

            auditLogService.logSystemEvent(
                    SystemLogType.INTERNAL_ERROR,
                    Severity.ERROR,
                    action,
                    resource,
                    details,
                    userId
            );
        } catch (Exception e) {
            log.warn("Failed to write failure audit log: {}", e.getMessage());
        }
    }

    UUID getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.userId();
        }
        return null;
    }
}
