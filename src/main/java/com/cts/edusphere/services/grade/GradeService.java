package com.cts.edusphere.services.grade;

import com.cts.edusphere.common.dto.grade.GradeRequest;
import com.cts.edusphere.common.dto.grade.GradeResponse;
import java.util.*;

public interface GradeService {
    GradeResponse createGrade(GradeRequest request);
    List<GradeResponse> getAllGrades();
    GradeResponse getGradeById(UUID id);
    GradeResponse updateGrade(UUID id, GradeRequest request);
    void deleteGrade(UUID id);
    List<GradeResponse> getGradesByStudent(UUID studentId);
    List<GradeResponse> getGradesByExam(UUID examId);


}
