package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.model.job.JobDto;
import fa.training.interviewmanagement.model.job.JobGetResponse;

import java.util.List;

public interface JobService {
    void createJob(JobDto jobto);
    JobGetResponse findAll(int page, int size);
    void delete(Integer id);
    Job findById(Integer jobId);
    List<Job> searchJob(String key);
    Job updateJob(Integer id, JobDto jobDetails);

    void changeStatus();
}
