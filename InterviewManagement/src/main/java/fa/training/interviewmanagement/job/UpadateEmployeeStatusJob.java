package fa.training.interviewmanagement.job;

import fa.training.interviewmanagement.service.JobService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Log4j2
public class UpadateEmployeeStatusJob {
    @Autowired
    JobService jobService;

    @Scheduled(cron = "${service.job-demo-execute-on: 0 */1 * * * *}")
    public void onExecute(){
        log.info("UpadateEmployeeStatusJob start on: {}", LocalDateTime.now());

        jobService.changeStatus();
        log.info("UpadateEmployeeStatusJob finished on: {}", LocalDateTime.now());

    }
}
