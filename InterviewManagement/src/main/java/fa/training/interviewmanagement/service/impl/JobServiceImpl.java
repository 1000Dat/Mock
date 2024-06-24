package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.model.job.JobDto;
import fa.training.interviewmanagement.model.job.JobGetResponse;
import fa.training.interviewmanagement.model.job.StatusJobEnum;
import fa.training.interviewmanagement.repository.JobRepository;
import fa.training.interviewmanagement.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobRepository jobRepository;

    public void createJob(JobDto jobDto){
        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setStartWork(jobDto.getStartWork());
        job.setEndWork(jobDto.getEndWork());
        job.setSkill(String.join(",", jobDto.getSkill()));
        job.setFr(jobDto.getFr());
        job.setT(jobDto.getT());
        job.setBenefits(String.join(",", jobDto.getBenefits()));
        job.setAddress(jobDto.getAddress());
        job.setLevel(String.join(",", jobDto.getLevel()));
        job.setDescription(jobDto.getDescription());
        job.setStatus(StatusJobEnum.DRAFT);
        jobRepository.save(job);
    }

    public JobGetResponse findAll(int page, int size) {
        JobGetResponse certGetResponse = new JobGetResponse();
        Page<Job> certEntityPage = jobRepository.findAll(PageRequest.of(page,size));
        List<JobGetResponse.JobResponse> dtoResponseList = certEntityPage.getContent()
                .stream().map(job -> {
                    JobGetResponse.JobResponse dto = new JobGetResponse.JobResponse();
                    dto.setJobId(job.getJobId());
                    dto.setTitle(job.getTitle());
                    dto.setSkill(job.getSkill());
                    dto.setStartWork(job.getStartWork());
                    dto.setEndWork(job.getEndWork());
                    dto.setLevel(job.getLevel());
                    dto.setStatus(job.getStatus());
                    return dto;
                }).collect(Collectors.toList());
        certGetResponse.setEntityList(dtoResponseList);
        certGetResponse.setTotalPage(certEntityPage.getTotalPages());
        certGetResponse.setTotalElements((int) certEntityPage.getTotalElements());

        return certGetResponse;
    }

    public void delete(Integer id){
        jobRepository.deleteById(id);
    }

    public Job findById(Integer jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Khong tim thay"));
    }

    public List<Job> searchJob (String key){
        return jobRepository.searchJob(key);
    }

    public Job updateJob(Integer id, JobDto jobDetails) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setTitle(jobDetails.getTitle());
            job.setStartWork(jobDetails.getStartWork());
            job.setEndWork(jobDetails.getEndWork());
            job.setFr(jobDetails.getFr());
            job.setT(jobDetails.getT());
            job.setAddress(jobDetails.getAddress());
            job.setStatus(jobDetails.getStatus());
            job.setSkill(String.join(",", jobDetails.getSkill()));
            job.setBenefits(String.join(",", jobDetails.getBenefits()));;
            job.setLevel(String.join(",", jobDetails.getLevel()));
            job.setDescription(jobDetails.getDescription());
            return jobRepository.save(job);
        } else {
            throw new RuntimeException("Job not found with id " + id);
        }
    }

    @Override
    public void changeStatus() {
     List<Job> jobList =   jobRepository.findByEndWork(LocalDate.now());
        for (Job job : jobList) {
            job.setStatus(StatusJobEnum.CLOSED);
            jobRepository.save(job);
        }
    }


}
