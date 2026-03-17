package com.cts.edusphere.exceptions.genericexceptions;

public class FailedToReviewAuditException extends RuntimeException {
    public FailedToReviewAuditException(String mssg) {
        super(mssg);
    }
}
