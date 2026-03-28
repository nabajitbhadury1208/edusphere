package com.cts.edusphere.exceptions.genericexceptions;

/**
 * Thrown when an audit review action could not be completed due to a server-side error.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.</p>
 */
public class FailedToReviewAuditException extends RuntimeException {

    /**
     * Constructs a new {@code FailedToReviewAuditException} with the specified detail message.
     *
     * @param mssg the detail message describing why the audit review failed
     */
    public FailedToReviewAuditException(String mssg) {
        super(mssg);
    }
}
