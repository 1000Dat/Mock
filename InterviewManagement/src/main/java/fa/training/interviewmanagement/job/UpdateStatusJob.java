package fa.training.interviewmanagement.job;

import fa.training.interviewmanagement.service.JobService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Log4j2
public class UpdateStatusJob {
    @Autowired
    JobService jobService;

    @Scheduled(cron = " 0 */1 * * * *")
    public void setStartWork(){
        log.info("UpadateEmployeeStatusJob start on: {}", LocalDateTime.now());

        jobService.changeStatusStartWork();
        log.info("UpadateEmployeeStatusJob finished on: {}", LocalDateTime.now());

    }

    @Scheduled(cron = "0 0 0 * * *")
    public void setEndWork(){
        log.info("UpadateEmployeeStatusJob start on: {}", LocalDateTime.now());

        jobService.changeStatusEndWork();
        log.info("UpadateEmployeeStatusJob finished on: {}", LocalDateTime.now());

    }
}
