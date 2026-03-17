package com.cts.edusphere.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import com.cts.edusphere.enums.SystemLogType;
import com.cts.edusphere.exceptions.genericexceptions.AuditLogNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.AuditNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.AuditsNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.ComplianceRecordNotCreatedException;
import com.cts.edusphere.exceptions.genericexceptions.ComplianceRecordNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.CourseNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.CoursesNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.DepartmentNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.EmailAlreadyExistsException;
import com.cts.edusphere.exceptions.genericexceptions.FileStorageException;
import com.cts.edusphere.exceptions.genericexceptions.InsufficientPermissionException;
import com.cts.edusphere.exceptions.genericexceptions.InternalServerErrorException;
import com.cts.edusphere.exceptions.genericexceptions.InvalidCredentialsException;
import com.cts.edusphere.exceptions.genericexceptions.InvalidPasswordException;
import com.cts.edusphere.exceptions.genericexceptions.InvalidTokenException;
import com.cts.edusphere.exceptions.genericexceptions.PasswordNotChangedException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceAlreadyExistsException;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.StorageException;
import com.cts.edusphere.exceptions.genericexceptions.StorageFileNotFoundException;
import com.cts.edusphere.exceptions.genericexceptions.TokenExpiredException;
import com.cts.edusphere.exceptions.genericexceptions.UnauthorizedAccessException;
import com.cts.edusphere.exceptions.genericexceptions.UserCreationFailedException;
import com.cts.edusphere.exceptions.genericexceptions.UserNotFoundException;

public final class ExceptionMap {

    private ExceptionMap() {
        // Private constructor to prevent instantiation
    }

    public static final Map<Class<? extends Exception>, SystemLogType> LOG_MAPPINGS = Map.ofEntries(
        // 404 Not Found
        Map.entry(ResourceNotFoundException.class, SystemLogType.RESOURCE_NOT_FOUND),
        Map.entry(StorageFileNotFoundException.class, SystemLogType.FILE_NOT_FOUND),
        Map.entry(UserNotFoundException.class, SystemLogType.USER_NOT_FOUND),
        Map.entry(DepartmentNotFoundException.class, SystemLogType.DEPARTMENT_NOT_FOUND),
        Map.entry(AuditNotFoundException.class, SystemLogType.AUDIT_NOT_FOUND),
        Map.entry(AuditsNotFoundException.class, SystemLogType.AUDITS_NOT_FOUND),
        Map.entry(AuditLogNotFoundException.class, SystemLogType.AUDIT_LOG_CREATE_FAILED),
        Map.entry(ComplianceRecordNotFoundException.class, SystemLogType.COMPLAINCE_RECORD_NOT_FOUND),
        Map.entry(CoursesNotFoundException.class, SystemLogType.COURSES_NOT_FOUND),
        Map.entry(CourseNotFoundException.class, SystemLogType.COURSE_NOT_FOUND),

        // 409 Conflict
        Map.entry(ResourceAlreadyExistsException.class, SystemLogType.RESOURCE_ALREADY_EXISTS),
        Map.entry(DataIntegrityViolationException.class, SystemLogType.DATA_INTEGRITY_VIOLATION),
        Map.entry(EmailAlreadyExistsException.class, SystemLogType.EMAIL_ALREADY_EXISTS),

        // 401 & 403 Security
        Map.entry(BadCredentialsException.class, SystemLogType.UNAUTHORIZED_ACCESS),
        Map.entry(InvalidCredentialsException.class, SystemLogType.DATA_INTEGRITY_VIOLATION),
        Map.entry(InvalidTokenException.class, SystemLogType.INVALID_TOKEN),
        Map.entry(TokenExpiredException.class, SystemLogType.INVALID_TOKEN),
        Map.entry(DisabledException.class, SystemLogType.ACCOUNT_DEACTIVATED),
        Map.entry(UnauthorizedAccessException.class, SystemLogType.UNAUTHORIZED_ACCESS),
        Map.entry(InsufficientPermissionException.class, SystemLogType.ACCESS_DENIED),
        Map.entry(AccessDeniedException.class, SystemLogType.ACCESS_DENIED),
        Map.entry(LockedException.class, SystemLogType.LOCKED_EXCEPTION),

        // 400 Bad Request
        Map.entry(InvalidPasswordException.class, SystemLogType.INVALID_PASSWORD),
        Map.entry(PasswordNotChangedException.class, SystemLogType.PASSWORD_CHANGE_ERROR),
        Map.entry(StorageException.class, SystemLogType.INVALID_FILE_FORMAT),

        // 500 Internal Error
        Map.entry(InternalServerErrorException.class, SystemLogType.INTERNAL_SERVER_ERROR),
        Map.entry(UserCreationFailedException.class, SystemLogType.USER_NOT_CREATED),
        Map.entry(FileStorageException.class, SystemLogType.FILE_STORAGE_EXCEPTION),
        Map.entry(ComplianceRecordNotCreatedException.class, SystemLogType.COMPLAINCE_RECORD_NOT_CREATED)
    );
}