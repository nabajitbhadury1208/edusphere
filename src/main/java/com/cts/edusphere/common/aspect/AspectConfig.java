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

/**
 * AOP aspect that cross-cuts every controller method in the EduSphere application
 * to provide transparent audit logging and request/response tracing.
 *
 * <p>Three advice types are applied around the {@link #controllerMethods()} pointcut:</p>
 * <ol>
 *   <li>{@link #logBefore(JoinPoint)} – logs entry into a controller method with its arguments</li>
 *   <li>{@link #logAfterSuccess(JoinPoint, Object)} – records a successful
 *       {@link SystemLogType#API_ACCESS} event in the audit log</li>
 *   <li>{@link #logAfterThrow(JoinPoint, Throwable)} – records an
 *       {@link SystemLogType#INTERNAL_ERROR} event whenever a controller method
 *       throws an exception</li>
 * </ol>
 *
 * <p>Audit-log write failures in the after-advices are caught and logged at
 * {@code WARN} level so that they never mask the original response or exception.</p>
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AspectConfig {

    private final AuditLogService auditLogService;

    /**
     * Pointcut that matches the execution of any method in any class under the
     * {@code com.cts.edusphere.controllers} package or its sub-packages.
     */
    @Pointcut("execution(* com.cts.edusphere.controllers..*.*(..))")
    public void controllerMethods() {
    }

    /**
     * Before-advice that logs the name and arguments of a controller method
     * at {@code INFO} level whenever it is invoked.
     *
     * @param joinPoint the join point representing the intercepted method call
     */
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering: {} with args: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * After-returning advice that writes a {@link SystemLogType#API_ACCESS} audit-log
     * entry whenever a controller method completes successfully.
     *
     * <p>The currently authenticated user's ID is extracted from the Spring Security
     * context; if the context is absent or the principal is anonymous, {@code null}
     * is recorded instead.</p>
     *
     * @param joinPoint the join point representing the intercepted method call
     * @param result    the value returned by the controller method (not used directly)
     */
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

    /**
     * After-throwing advice that logs the exception and writes an
     * {@link SystemLogType#INTERNAL_ERROR} audit-log entry whenever a controller
     * method propagates an exception.
     *
     * @param joinPoint the join point representing the intercepted method call
     * @param ex        the throwable that was raised by the controller method
     */
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

    /**
     * Retrieves the UUID of the currently authenticated user from the Spring
     * Security context.
     *
     * @return the authenticated user's UUID, or {@code null} if the security
     *         context holds no authentication or the principal is not a
     *         {@link UserPrincipal}
     */
    UUID getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.userId();
        }
        return null;
    }
}
