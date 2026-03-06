package com.cts.edusphere.services.exam;

import com.cts.edusphere.common.dto.exam.ExamRequest;
import com.cts.edusphere.common.dto.exam.ExamResponse;
import com.cts.edusphere.enums.Status;

import java.util.List;
import java.util.UUID;

public interface ExamService {
    ExamResponse createExam(ExamRequest request);
    List<ExamResponse> getAllExams();
    ExamResponse getExamById(UUID id);
    ExamResponse updateExam(UUID id, ExamRequest request);
    ExamResponse partialUpdateExam(UUID id, ExamRequest request);
    ExamResponse updateExamStatus(UUID id, Status status);
    void deleteExam(UUID id);
    List<ExamResponse> getExamsByCourse(UUID courseId);


}
