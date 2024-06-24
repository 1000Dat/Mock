package fa.training.interviewmanagement.model.candidate;

import lombok.Data;

import java.util.List;

@Data
public class CandidateGetResponse {
    private List<CandidateResponse> candidateResponsesList;
    private Integer totalPage;
    private Integer totalElements;
    @Data
    public static class CandidateResponse {
        private Integer candId;
        private String name;
        private String email;
        private String phone;
        private CandidateEnum.CurrentPosition currentPosition;
        private String ownerHR;
        private CandidateEnum.CandidateStatus status;
    }
}
