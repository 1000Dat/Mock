package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.candidate.CandidateDto;
import fa.training.interviewmanagement.model.candidate.CandidateGetResponse;
import fa.training.interviewmanagement.repository.CandidateRepository;
import fa.training.interviewmanagement.repository.UserRepository;
import fa.training.interviewmanagement.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createCandidate(CandidateDto candidateDto) {
        UserEntity recruiter = userRepository.findByUsername(candidateDto.getOwnerHR());
        Candidate candidate = new Candidate();
        candidate.setName(candidateDto.getName());
        candidate.setEmail(candidateDto.getEmail());
        candidate.setGender(candidateDto.getGender());
        candidate.setDob(candidateDto.getDob());
        candidate.setAddress(candidateDto.getAddress());
        candidate.setPhone(candidateDto.getPhone());
        candidate.setProfessionalInformation(candidateDto.getProfessionalInformation());
        candidate.setCurrentPosition(candidateDto.getCurrentPosition());
        candidate.setSkill(candidateDto.getSkill());
        candidate.setHighestLevel(candidateDto.getHighestLevel());
        candidate.setOwnerHR(String.valueOf(recruiter));
        candidate.setStatus(candidateDto.getStatus());
        candidate.setNote(candidateDto.getNote());
        candidate.setYearOfExperience(candidateDto.getYearOfExperience());
        candidate.setCandidateCV(candidateDto.getCandidateCV());
        candidateRepository.save(candidate);
    }

    @Override
    public CandidateGetResponse findAll(int page, int size) {
        CandidateGetResponse candidateGetResponse = new CandidateGetResponse();
        Page<Candidate> candidatePage = candidateRepository.findAll(PageRequest.of(page, size));
        List<CandidateGetResponse.CandidateResponse> candidateResponseList = candidatePage.getContent().stream().map(
                candidate -> {
                    CandidateGetResponse.CandidateResponse candidateResponse = new CandidateGetResponse.CandidateResponse();
                    candidateResponse.setCandId(candidate.getCandId());
                    candidateResponse.setName(candidate.getName());
                    candidateResponse.setEmail(candidate.getEmail());
                    candidateResponse.setPhone(candidate.getPhone());
                    candidateResponse.setCurrentPosition(candidate.getCurrentPosition());
                    candidateResponse.setOwnerHR(candidate.getOwnerHR());
                    candidateResponse.setStatus(candidate.getStatus());
                    return candidateResponse;
                }).collect(Collectors.toList());
        candidateGetResponse.setCandidateResponsesList(candidateResponseList);
        candidateGetResponse.setTotalPage(candidatePage.getTotalPages());
        candidateGetResponse.setTotalElements((int) candidatePage.getTotalElements());
        return candidateGetResponse;
    }

    @Override
    public void deleteCandidate(Integer id) {
        candidateRepository.deleteById(id);

    }

    @Override
    public Candidate findById(Integer id) {
        return candidateRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate Not Found"));

    }

    @Override
    public CandidateGetResponse searchCandidates(String field, String keyword, int page, int size) {
        Page<Candidate> candidatePage;
        Pageable pageable = PageRequest.of(page, size);

        switch (field.toLowerCase()) {
            case "name":
                candidatePage = candidateRepository.findByNameContaining(keyword, pageable);
                break;
            case "email":
                candidatePage = candidateRepository.findByEmailContaining(keyword, pageable);
                break;
            case "phone":
                candidatePage = candidateRepository.findByPhoneContaining(keyword, pageable);
                break;
            case "currentposition":
                candidatePage = candidateRepository.findByCurrentPositionContaining(keyword, pageable);
                break;
            case "ownerhr":
                candidatePage = candidateRepository.findByOwnerHRContaining(keyword, pageable);
                break;
            case "status":
                candidatePage = candidateRepository.findByStatusContaining(keyword, pageable);
                break;
            default:
                candidatePage = Page.empty(pageable);
        }

        List<CandidateGetResponse.CandidateResponse> candidateResponseList = candidatePage.getContent().stream()
                .map(this::convertToCandidateResponse)
                .collect(Collectors.toList());

        CandidateGetResponse candidateGetResponse = new CandidateGetResponse();
        candidateGetResponse.setCandidateResponsesList(candidateResponseList);
        candidateGetResponse.setTotalPage(candidatePage.getTotalPages());
        candidateGetResponse.setTotalElements((int) candidatePage.getTotalElements());

        return candidateGetResponse;
    }

    private CandidateGetResponse.CandidateResponse convertToCandidateResponse(Candidate candidate) {
        CandidateGetResponse.CandidateResponse candidateResponse = new CandidateGetResponse.CandidateResponse();
        candidateResponse.setCandId(candidate.getCandId());
        candidateResponse.setName(candidate.getName());
        candidateResponse.setEmail(candidate.getEmail());
        candidateResponse.setPhone(candidate.getPhone());
        candidateResponse.setCurrentPosition(candidate.getCurrentPosition());
        candidateResponse.setOwnerHR(candidate.getOwnerHR());
        candidateResponse.setStatus(candidate.getStatus());
        return candidateResponse;
    }

    @Override
    public Candidate updateCandidate(Integer id, CandidateDto candidateDto) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setName(candidateDto.getName());
            candidate.setEmail(candidateDto.getEmail());
            candidate.setGender(candidateDto.getGender());
            candidate.setDob(candidateDto.getDob());
            candidate.setAddress(candidateDto.getAddress());
            candidate.setPhone(candidateDto.getPhone());
            candidate.setProfessionalInformation(candidateDto.getProfessionalInformation());
            candidate.setCurrentPosition(candidateDto.getCurrentPosition());
            candidate.setSkill(candidateDto.getSkill());
            candidate.setHighestLevel(candidateDto.getHighestLevel());
            candidate.setOwnerHR(candidateDto.getOwnerHR());
            candidate.setStatus(candidateDto.getStatus());
            candidate.setNote(candidateDto.getNote());
            candidate.setYearOfExperience(candidateDto.getYearOfExperience());
            candidate.setCandidateCV(candidateDto.getCandidateCV());
            return candidateRepository.save(candidate);
        } else {
            throw new RuntimeException("Candidate Not Found");
        }
    }

    @Override
    public List<UserEntity> getRecruiter() {
        return userRepository.findRecruiter();
    }

//    @Override
//    public List<UserEntity> getRecruiter() {
//        return userRepository.findRecruiter();
//    }
}
