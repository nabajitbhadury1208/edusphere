package com.cts.edusphere.common.aspect;

import java.util.Arrays;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.services.audit_log.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AspectConfig {
    private final AuditLogService auditLogService;

    // Run during execution time, and on these packages
    @Pointcut("execution(* com.cts.edusphere.controllers..*.*(..))")
    public void controllerMethods() {
    }

    // Runs before the controller works
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Called: {}", joinPoint.getSignature().getName()); // Method being called
        log.info("Called: {}", Arrays.toString(joinPoint.getArgs())); // Arguments being called

    }

    // Runs after controller has done its job
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint) {
       handleAudit(joinPoint, null);

    }

    @AfterThrowing(pointcut = "controllerMethods()")
    public void logAfterThrow(JoinPoint joinPoint, Throwable ex) {
       handleAudit(joinPoint, ex);
    }

    private void handleAudit(JoinPoint joinPoint, Throwable ex) {
        UUID userId = getUserId();
        String action = joinPoint.getSignature().getName();

        String resource = joinPoint.getTarget().getClass().getSimpleName();

        if (userId == null) {
            log.info("Anonymous action: {} on {}. Skipping database audit log.", action, resource);
            return;
        }

        if (ex != null) {
            action = action + " (FAILED: " + ex.getClass().getSimpleName() + ")";
            log.error("Method {} failed with: {}", action, ex.getMessage());
        }

        try {
            AuditLogRequestDTO auditLogRequestDTO = new AuditLogRequestDTO(userId, action, resource);
            auditLogService.createLog(auditLogRequestDTO, userId);
            log.info("{} successfully logged action: {}", resource, action);
        } catch (Exception e) {
            log.error("Failed to persist audit log: {}", e.getMessage());
        }
    }

    UUID getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.userId();
        }

        return null;
    }
}