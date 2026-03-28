package com.cts.edusphere.aspects;

import com.cts.edusphere.enums.AuditStatus;
import com.cts.edusphere.modules.audit.Audit;
import com.cts.edusphere.repositories.audit.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Spring AOP aspect that automatically generates compliance audit records whenever
 * a method annotated with {@link ComplianceAudit} returns successfully.
 *
 * <p>This aspect intercepts the return of any {@code @ComplianceAudit}-annotated method,
 * extracts the entity identifier from the returned object, and persists a new
 * {@link com.cts.edusphere.modules.audit.Audit} record with status
 * {@link com.cts.edusphere.enums.AuditStatus#PENDING} via {@link AuditRepository}.</p>
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAutoGeneratorAspect {

    private final AuditRepository auditRepository;

    /**
     * After-returning advice that fires whenever a method annotated with
     * {@code @ComplianceAudit} completes without throwing an exception.
     *
     * <p>The advice reads the {@link ComplianceAudit} metadata from the intercepted
     * method, extracts the entity {@link UUID} from the returned value, builds a
     * pending {@link com.cts.edusphere.modules.audit.Audit} record, and saves it.
     * Any failure during this process is caught and logged so that it never disrupts
     * the original business flow.</p>
     *
     * @param joinPoint provides reflective access to the intercepted method and its
     *                  declaring type
     * @param result    the value returned by the intercepted method; may be
     *                  {@code null} if the method returns {@code void} or explicitly
     *                  returns {@code null}
     */
    @AfterReturning(pointcut = "@annotation(com.cts.edusphere.aspects.ComplianceAudit)", returning = "result")
    @Transactional
    public void autoGenerateComplianceAudit(JoinPoint joinPoint, Object result) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            ComplianceAudit annotation = method.getAnnotation(ComplianceAudit.class);

            UUID entityId = extractEntityId(result);

            Audit pendingAudit = Audit.builder()
                    .entityType(annotation.entityType())
                    .entityId(entityId)
                    .scope(annotation.scope())
                    .status(AuditStatus.PENDING)
                    .build();

            auditRepository.save(pendingAudit);
            log.info("Auto-generated pending compliance audit ticket for event type: {}", annotation.entityType());

        } catch (Exception e) {
            log.error("Failed to auto-generate compliance audit queue ticket: {}", e.getMessage(), e);
        }
    }

    /**
     * Attempts to extract a {@link UUID} entity identifier from an arbitrary result
     * object using a sequence of reflection-based strategies.
     *
     * <ol>
     *   <li>Calls {@code getId()} if the method exists and returns a {@link UUID}.</li>
     *   <li>Falls back to {@code id()} (record-style accessor).</li>
     *   <li>Scans all public no-arg methods whose name ends with {@code "Id"} and
     *       whose return type is {@link UUID}, returning the first match.</li>
     * </ol>
     *
     * @param result the object from which the entity identifier should be extracted;
     *               may be {@code null}
     * @return the extracted {@link UUID}, or {@code null} if the result is
     *         {@code null}, no suitable accessor is found, or reflection fails
     */
    private UUID extractEntityId(Object result) {
        if (result == null) return null;

        try {
            Method getIdMethod = result.getClass().getMethod("getId");
            Object idVal = getIdMethod.invoke(result);
            if (idVal instanceof UUID) {
                return (UUID) idVal;
            }
        } catch (NoSuchMethodException e) {
            try {
                Method idAccessor = result.getClass().getMethod("id");
                Object idVal = idAccessor.invoke(result);
                if (idVal instanceof UUID) {
                    return (UUID) idVal;
                }
            } catch (Exception ex) {
                // fall through
            }
            try {
                for (Method m : result.getClass().getMethods()) {
                    if (m.getName().endsWith("Id") && m.getParameterCount() == 0 && m.getReturnType().equals(UUID.class)) {
                        return (UUID) m.invoke(result);
                    }
                }
            } catch (Exception ex) {
            }
        } catch (Exception e) {
            log.warn("Could not extract UUID entityId from returned object of type {}", result.getClass().getName());
        }

        return null;
    }
}