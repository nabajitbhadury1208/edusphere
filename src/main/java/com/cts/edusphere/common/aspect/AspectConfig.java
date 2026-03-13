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
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.services.audit_log.AuditLogService;
import com.cts.edusphere.services.user.UserServiceImpl;

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
    public void logAfter(JoinPoint joinPoint, Object result) {
        String action = joinPoint.getSignature().getName();
        String resource = joinPoint.getSignature().getClass().getSimpleName();

        AuditLogRequestDTO auditLogRequestDTO = new AuditLogRequestDTO(getUserId(), action, resource);
        auditLogService.createLog(auditLogRequestDTO);

        log.info("{} called the method {}(). Returned with a success", resource, action);

    }

    @AfterThrowing(pointcut = "controllerMethods()")
    public void logAfterThrow(JoinPoint joinPoint, Throwable ex) {
        String action = joinPoint.getSignature().getName();
        String resource = joinPoint.getSignature().getClass().getSimpleName();
        String exceptionName = ex.getClass().getSimpleName();
        String exceptionMssg = ex.getMessage();

        log.info("{}: {} called the method {}. Returned with an exception -> {}", exceptionName, resource, action,
                exceptionMssg);

        AuditLogRequestDTO auditLogRequestDTO = new AuditLogRequestDTO(getUserId(), action, resource);

        auditLogService.createLog(auditLogRequestDTO);
    }

    UUID getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.userId();
        }

        return null;
    }
}