package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.Interview;
import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.candidate.CandidateEnum;
import fa.training.interviewmanagement.model.interview.InterviewCandidate;
import fa.training.interviewmanagement.model.interview.InterviewDto;
import fa.training.interviewmanagement.model.interview.InterviewGetResponse;
import fa.training.interviewmanagement.repository.CandidateRepository;
import fa.training.interviewmanagement.repository.InterviewRepository;
import fa.training.interviewmanagement.repository.JobRepository;
import fa.training.interviewmanagement.repository.UserRepository;
import fa.training.interviewmanagement.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createInterview(InterviewDto dto) {

        Optional<Candidate> candidate = candidateRepository.findById(dto.getCandidates().get(0).getId());
        Job job = jobRepository.findFirstByTitle(dto.getJob());
        UserEntity recruiter = userRepository.findByUsername(dto.getRecruiterOwner());
        List<UserEntity> interviewerInterviews = new ArrayList<>();
        if (dto.getInterview() != null && !dto.getInterview().trim().isEmpty()) {
            // Tách chuỗi tên người dùng thành danh sách
            List<String> interviewerUsernames = Arrays.asList(dto.getInterview().split(","));
            for (String username : interviewerUsernames) {
                // Tìm kiếm người dùng theo tên người dùng
                UserEntity user = userRepository.findByUsername(username.trim());
                if (user != null) {
                    // Thêm người dùng vào danh sách người phỏng vấn
                    interviewerInterviews.add(user);
                }
            }
        }

        // Tạo đối tượng Interview và thiết lập các thuộc tính
        Interview interview = new Interview();
        interview.setScheduleTitle(dto.getScheduleTitle());
        interview.setScheduleTime(dto.getScheduleTime());
        interview.setCandidate(candidate.get());
        interview.setScheduleFrom(dto.getScheduleFrom());
        interview.setScheduleTo(dto.getScheduleTo());
        interview.setJob(job);
        interview.setInterviewers(interviewerInterviews);
        interview.setLocation(dto.getLocation());
        interview.setRecruiter(recruiter);
        interview.setMeetingId(dto.getMeetingID());
        interview.setStatus("New");
        interview.setResult("N/A");

        // Lưu đối tượng Interview vào cơ sở dữ liệu
        interviewRepository.save(interview);

        // Cập nhật trạng thái của ứng viên và lưu lại
        candidate.get().setStatus(CandidateEnum.CandidateStatus.WaitingForInterview);
        candidateRepository.save(candidate.get());
    }


    @Override
    public InterviewGetResponse findAll(int page, int size) {
        InterviewGetResponse interviewPageDto = new InterviewGetResponse();
        Page<Interview> interviewPage = interviewRepository.findAll(PageRequest.of(page, size));

        List<InterviewGetResponse.InterviewResponse> dtoResponseList = interviewPage.getContent().stream().map(entity -> {
            InterviewGetResponse.InterviewResponse dtoResponse = new InterviewGetResponse.InterviewResponse();
            dtoResponse.setInterviewId(entity.getInterviewId());

            String interviewers = entity.getInterviewers().stream()
                    .map(UserEntity::getUsername)
                    .collect(Collectors.joining(", "));
            dtoResponse.setInterviewer(interviewers);
            dtoResponse.setTitle(entity.getScheduleTitle());
            dtoResponse.setCandidateName(entity.getCandidate().getName());
            String schedule = entity.getScheduleTime().toString() + "  "
                    + entity.getScheduleFrom().toString() + " - "
                    + entity.getScheduleTo().toString();
            dtoResponse.setSchedule(schedule);
            dtoResponse.setJob(entity.getJob().getTitle());
            dtoResponse.setResult(entity.getResult());
            dtoResponse.setStatus(entity.getStatus());
            return dtoResponse;
        }).collect(Collectors.toList());

        interviewPageDto.setInterviewResponseList(dtoResponseList);
        interviewPageDto.setTotalPage(interviewPage.getTotalPages());
        interviewPageDto.setTotalElements((int) interviewPage.getTotalElements());
        return interviewPageDto;
    }

    @Override
    public InterviewGetResponse searchInterviewer(String keyword, String status, int page, int size) {
        Page<Interview> interviewPage;
        Pageable pageable = PageRequest.of(page, size);

        if ((keyword != null && !keyword.isEmpty()) && (status != null && !status.isEmpty())) {
            interviewPage = interviewRepository.findByKeywordAndStatusContainingIgnoreCase(keyword, status, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            interviewPage = interviewRepository.findByKeywordInAnyFieldIgnoreCase(keyword, pageable);
        } else if (status != null && !status.isEmpty()) {
            interviewPage = interviewRepository.findByStatusContainingIgnoreCase(status, pageable);
        } else {
            interviewPage = interviewRepository.findAll(pageable);
        }

        List<InterviewGetResponse.InterviewResponse> dtoResponseList = interviewPage.getContent().stream().map(entity -> {
            InterviewGetResponse.InterviewResponse dtoResponse = new InterviewGetResponse.InterviewResponse();
            dtoResponse.setInterviewId(entity.getInterviewId());
            String interviewers = entity.getInterviewers().stream()
                    .map(UserEntity::getUsername)
                    .collect(Collectors.joining(", "));
            dtoResponse.setInterviewer(interviewers);
            dtoResponse.setTitle(entity.getScheduleTitle());
            dtoResponse.setCandidateName(entity.getCandidate().getName());
            String schedule = entity.getScheduleTime().toString() + "  "
                    + entity.getScheduleFrom().toString() + " - "
                    + entity.getScheduleTo().toString();

            dtoResponse.setSchedule(schedule);
            dtoResponse.setJob(entity.getJob().getTitle());
            dtoResponse.setResult(entity.getResult());
            dtoResponse.setStatus(entity.getStatus());
            return dtoResponse;

        }).collect(Collectors.toList());
        InterviewGetResponse interviewPageDto = new InterviewGetResponse();
        interviewPageDto.setInterviewResponseList(dtoResponseList);
        interviewPageDto.setTotalPage(interviewPage.getTotalPages());
        interviewPageDto.setTotalElements((int) interviewPage.getTotalElements());
        return interviewPageDto;
    }

    @Override
    public List<Job> getAllJob() {
        return jobRepository.findTitlesByOpen();
    }

    @Override
    public List<Candidate> getAllCandidate() {
        return candidateRepository.findAllOpen();
    }

    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAllRecruiters();
    }

    @Override
    public List<UserEntity> getAllUserInterviewer() {
        return userRepository.findAllInterviewers();
    }


    @Override
    public InterviewGetResponse.InterviewResponse getInterviewById(Integer id) {
        Interview interview = interviewRepository.findById(id).orElse(null);
        if (interview == null) {
            return null;
        }

        InterviewGetResponse.InterviewResponse dto = new InterviewGetResponse.InterviewResponse();
        dto.setInterviewId(interview.getInterviewId());
        dto.setTitle(interview.getScheduleTitle());
        dto.setCandidateName(interview.getCandidate().getName());
        String schedule = interview.getScheduleTime().toString() + " Form "
                + interview.getScheduleFrom().toString() + " To "
                + interview.getScheduleTo().toString();

        dto.setSchedule(schedule);
        dto.setJob(interview.getJob().getTitle());
        dto.setInterviewer(interview.getInterviewers().stream().map(UserEntity::getUsername).collect(Collectors.joining(",")));
        dto.setRecruiterOwner(interview.getRecruiter().getUsername());
        dto.setMeetingID(interview.getMeetingId());
        dto.setNotes(interview.getNotes());
        dto.setStatus(interview.getStatus());
        dto.setResult(interview.getResult());
        dto.setLocation(interview.getLocation());
        return dto;
    }


    @Transactional
    public void EditInterview(InterviewDto request, Integer id) throws Exception {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(id);
            if (interviewOptional.isEmpty()) {
                throw new Exception("Interview not found with id: " + id);
            }

            Interview interview = interviewOptional.get();


            Optional<Candidate> candidate = candidateRepository.findById(request.getCandidates().get(0).getId());
            Job job = jobRepository.findFirstByTitle(request.getJob());
            UserEntity recruiter = userRepository.findByUsername(request.getRecruiterOwner());


            List<UserEntity> interviewers = new ArrayList<>();
            if (request.getInterview() != null) {
                List<String> interviewerUsernames = Arrays.asList(request.getInterview().split(","));
                for (String username : interviewerUsernames) {
                    UserEntity user = userRepository.findByUsername(username);
                    if (user != null) {
                        interviewers.add(user);
                    }
                }
            }
            interview.setScheduleTitle(request.getScheduleTitle());
            interview.setScheduleTime(request.getScheduleTime());
            interview.setCandidate(candidate.get());
            interview.setScheduleFrom(request.getScheduleFrom());
            interview.setScheduleTo(request.getScheduleTo());
            interview.setJob(job);
            interview.setInterviewers(interviewers);
            interview.setLocation(request.getLocation());
            interview.setRecruiter(recruiter);
            interview.setMeetingId(request.getMeetingID());
            interview.setResult(request.getResult());
            interview.setNotes(request.getNotes());

            interviewRepository.save(interview);

        } catch (DataIntegrityViolationException e) {
            throw new Exception("Data integrity violation while updating interview with id: " + id, e);
        } catch (Exception e) {
            throw new Exception("Error editing interview with id: " + id, e);
        }
    }


    @Override
    public InterviewDto findById(Integer id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        InterviewDto interviewPostDto = new InterviewDto();
        interviewPostDto.setInterviewId(interview.getInterviewId()); // Set the ID

        InterviewCandidate candidateDto = new InterviewCandidate(
                interview.getCandidate().getCandId(),
                interview.getCandidate().getName(),
                interview.getCandidate().getEmail()
        );
        interviewPostDto.setCandidates(Collections.singletonList(candidateDto));
        interviewPostDto.setJob(interview.getJob().getTitle());

        List<String> interviewerNames = interview.getInterviewers()
                .stream()
                .map(UserEntity::getUsername)
                .collect(Collectors.toList());
        interviewPostDto.setInterview(String.join(",", interviewerNames));

        interviewPostDto.setRecruiterOwner(interview.getRecruiter().getUsername());
        interviewPostDto.setScheduleTitle(interview.getScheduleTitle());
        interviewPostDto.setScheduleTime(interview.getScheduleTime());
        interviewPostDto.setScheduleFrom(interview.getScheduleFrom());
        interviewPostDto.setScheduleTo(interview.getScheduleTo());
        interviewPostDto.setLocation(interview.getLocation());
        interviewPostDto.setMeetingID(interview.getMeetingId());
        interviewPostDto.setResult(interview.getResult());
        interviewPostDto.setStatus(interview.getStatus());
        interviewPostDto.setNotes(interview.getNotes());
        return interviewPostDto;
    }

    @Transactional
    public void cancelSchedule(Integer id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found: " + id));
        interview.setStatus("Cancelled");
        interviewRepository.save(interview);
    }
}
