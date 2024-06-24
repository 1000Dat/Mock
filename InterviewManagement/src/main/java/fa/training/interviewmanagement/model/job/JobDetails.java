package fa.training.interviewmanagement.model.job;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobDetails {
    Integer jobId;
    String title;
    String skill;
    String fr;
    String t;
    String benefits;
    String level;
    LocalDate startWork;
    LocalDate endWork;
    String status;
    String address;
    String description;
}
