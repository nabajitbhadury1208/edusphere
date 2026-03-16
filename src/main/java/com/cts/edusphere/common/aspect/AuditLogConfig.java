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

import com.cts.edusphere.common.dto.audit.AuditRequestDTO;
import com.cts.edusphere.common.dto.audit.AuditResponseDTO;
import com.cts.edusphere.common.dto.audit_log.AuditLogRequestDTO;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.services.audit.AuditService;
import com.cts.edusphere.services.audit_log.AuditLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogConfig {
    private final AuditLogService auditLogService;
    private final AuditService auditService;

    // Run during execution time, and on these packages
    // @Pointcut("execution(* com.cts.edusphere.controllers..*.*(..)) " +
    // "&& !execution(* com.cts.edusphere.controllers.AuthController.login(..);" +
    // "&& !execution(* com.cts.edusphere.controllers.AuthController.register(..);")
    // // exclude login and register endpoint
    @Pointcut("execution(* com.cts.edusphere.controllers..*.*(..))")
    public void controllerMethods() {
    }

    // Runs BEFORE the controller works
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Class called: {}", joinPoint.getTarget().getClass().getSimpleName()); // Class being called
        log.info("Methods invoked: : {}", joinPoint.getSignature().getName()); // Method being called
    }

    // Runs AFTER controller has done its job
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        handleAudit(joinPoint, result, null);
    }

    // Runs AFTER THROWING an exception
    @AfterThrowing(pointcut = "controllerMethods()")
    public void logAfterThrow(JoinPoint joinPoint, Throwable exception) {
        handleAudit(joinPoint, null, exception);
    }

    private void handleAudit(JoinPoint joinPoint, Object result, Throwable exception) {
        UUID userId = getUserId();
        String resource = joinPoint.getTarget().getClass().getSimpleName(); // Get class name
        String action = joinPoint.getSignature().getName(); // Get method name

        if (userId == null) {
            log.info("Anonymous user {}: Action- {}. Action not logged in db.", resource, action);
        }

        try {
            if (result != null && exception == null) {
                log.info("User: {}, Resource: {}, Action: {}", userId, resource, action);
                
                AuditRequestDTO auditRequestDTO = new AuditRequestDTO(userId, action, resource, null);
                auditService.createAudit(auditRequestDTO);
                
                return;
            }

            if (result == null && exception != null) {
                action = action + " (Exception with name: " + exception.getClass().getSimpleName() + ")";
                log.error("{}: Method {} failed with: {}", resource, action, exception.getMessage());
                log.error(action);
                
                AuditLogRequestDTO auditLogRequestDTO = new AuditLogRequestDTO(userId,
                action, resource);
                auditLogService.createLog(auditLogRequestDTO, userId);
            }
        } catch (Exception e) {
            log.error("Failed to add log to db: {}", e.getMessage());
        }

        // try {
        // AuditLogRequestDTO auditLogRequestDTO = new AuditLogRequestDTO(userId,
        // action, resource);
        // auditLogService.createLog(auditLogRequestDTO, userId);
        // log.info("{}: {} successfully logged action: {}", userId, resource, action);
        // } catch (Exception e) {
        // log.error("Failed to persist audit log: {}", e.getMessage());
        // }
    }

    UUID getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.userId();
        }

        return null;
    }
}