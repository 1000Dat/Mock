package fa.training.interviewmanagement.model.job;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobGetResponse {
    private List<JobResponse> EntityList;
    private Integer totalPage;
    private Integer totalElements;

    @Data
    public static class JobResponse {
        Integer jobId;
        String title;
        String skill;
        LocalDate startWork;
        LocalDate endWork;
        String level;
        StatusJobEnum status;
    }
}
