package fa.training.interviewmanagement.service.processor;

import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.model.job.JobDto;
import fa.training.interviewmanagement.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

@Component
public class JobValidateProcessor {
    @Autowired
    JobRepository jobRepository;
    public BindingResult  isValidate(BindingResult result, JobDto jobDto) {


        boolean isValidDate = validateDate(jobDto.getStartWork(), jobDto.getEndWork());
        if (!isValidDate) {
            result.rejectValue("startWork", "", "Ngày bắt đầu phải trước ngày kết thúc");
        }
        try {
            if (jobDto.getFr() != null && jobDto.getT() != null) {
                int salaryFrom = Integer.parseInt(jobDto.getFr());
                int salaryTo = Integer.parseInt(jobDto.getT());
                if (salaryFrom > salaryTo) {
                    result.rejectValue("fr", "", "Giá trị 'from' phải nhỏ hơn giá trị 'to'");
                }
            }
        } catch (NumberFormatException e) {
            result.rejectValue("fr", "", "Giá trị 'from' và 'to' phải là số hợp lệ");
        }

        return result;
    }


    public boolean validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            return false;
        }
        return true;
    }

    public  void validateScheduleTime(JobDto jobDto, BindingResult bindingResult) {
        if (jobDto.getStartWork() == null || jobDto.getStartWork().isBefore(LocalDate.now())) {
            bindingResult.rejectValue("startWork", "error.startWork", "Thời gian lịch trình không được là ngày hôm qua hoặc quá khứ");
        }
    }
    public boolean checkDuplicateTitle(String title,Integer id) {
        Job jobDuplicateCount = jobRepository.findFirstByTitle(title);
        if (jobDuplicateCount != null && jobDuplicateCount.getJobId() != id) {
            return true;
        }
        return false;
    }
    public void validateTitle(JobDto jobDto, BindingResult bindingResult) {
        if (checkDuplicateTitle(jobDto.getTitle() , jobDto.getJobId()) ) {
            bindingResult.rejectValue("title", "error.title", "Tên này đã tồn tại");
        }

    }

    public boolean checkDuplicateTitle(String title) {
        int jobDuplicateCount = jobRepository.countByTitle(title);
        return jobDuplicateCount > 0;
    }
}