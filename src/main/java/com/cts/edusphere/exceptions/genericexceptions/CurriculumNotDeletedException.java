package com.cts.edusphere.exceptions.genericexceptions;

public class CurriculumNotDeletedException extends RuntimeException {
    public CurriculumNotDeletedException(String mssg) {
        super(mssg);
    }
}
