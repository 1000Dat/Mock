package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.candidate.CandidateDto;
import fa.training.interviewmanagement.model.candidate.CandidateGetResponse;

import java.util.List;

public interface CandidateService {
    void createCandidate(CandidateDto candidateDto);
    CandidateGetResponse findAll(int page, int size);
    void deleteCandidate(Integer id);
    Candidate findById(Integer id);
    public CandidateGetResponse searchCandidates(String field, String keyword, int page, int size);
    Candidate updateCandidate(Integer id, CandidateDto candidateDto);
    List<UserEntity> getRecruiter();
}
