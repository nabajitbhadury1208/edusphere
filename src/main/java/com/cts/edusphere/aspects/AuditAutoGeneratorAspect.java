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

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAutoGeneratorAspect {

    private final AuditRepository auditRepository;

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