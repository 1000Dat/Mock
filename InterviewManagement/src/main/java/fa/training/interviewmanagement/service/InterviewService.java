package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.Interview;
import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.interview.InterviewDto;
import fa.training.interviewmanagement.model.interview.InterviewEditDto;
import fa.training.interviewmanagement.model.interview.InterviewGetResponse;

import java.util.List;

public interface InterviewService {
    void createInterview(InterviewDto interviewDto);
    InterviewGetResponse findAll(int page, int size);

    InterviewGetResponse searchInterviewer(String keyword, String status, int page, int size);
    List<Candidate> getAllCandidate();
    List<Job> getAllJob();
    List<UserEntity> getAllUser();
    List<UserEntity> getAllUserInterviewer();
    InterviewGetResponse.InterviewResponse getInterviewById(Integer id);

    void EditInterview(InterviewEditDto interviewDto, Integer id) throws Exception;
    InterviewEditDto findById(Integer id);
    void cancelSchedule(Integer id);

    void Interviewed(InterviewDto request, Integer id) throws Exception ;
}
