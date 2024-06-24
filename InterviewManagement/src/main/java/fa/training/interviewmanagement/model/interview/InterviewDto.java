package fa.training.interviewmanagement.model.interview;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InterviewDto {
    private Integer interviewId;
    private String scheduleTitle;
    private String candidateName;
    private LocalDate scheduleTime;
    private LocalTime scheduleFrom;
    private LocalTime scheduleTo;
    private String location;
    private String job;
    private String interview;
    private String recruiterOwner;
    private String meetingID;
    private String notes;
    private String status;
    private String result;
}
