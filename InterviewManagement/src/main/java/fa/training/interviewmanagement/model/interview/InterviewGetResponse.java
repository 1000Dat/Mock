package fa.training.interviewmanagement.model.interview;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class InterviewGetResponse {
    private List<InterviewResponse> interviewResponseList;
    private Integer totalPage;
    private Integer totalElements;

    @Data
    public static class InterviewResponse {
        private Integer interviewId;
        private String title;
        private String candidateName;
        private String schedule;
        private String job;
        private String interviewer;
        private String result;
        private String status;
        private String notes;
        private String recruiterOwner;
        private String meetingID;
        private String location;
    }
}
