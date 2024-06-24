package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.interview.InterviewDto;
import fa.training.interviewmanagement.model.interview.InterviewGetResponse;
import fa.training.interviewmanagement.service.InterviewService;
import fa.training.interviewmanagement.service.processor.InterviewValidateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewValidateProcessor interviewValidator;

    @GetMapping("/showCreate")
    public String showAddInterview(Model model) {
        model.addAttribute("interviewPostDto", new InterviewDto());
        List<Job> jobList = interviewService.getAllJob();
        model.addAttribute("jobsList", jobList);
        List<Candidate> candidateList = interviewService.getAllCandidate();
        model.addAttribute("candidateList", candidateList);
        List<UserEntity> userList = interviewService.getAllUser();
        model.addAttribute("userList", userList);
        List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();
        model.addAttribute("userInterviewerList", userInterviewerList);
        return "interviewCreate";
    }

    @PostMapping("/createInterview")
    public String createInterview(@ModelAttribute("interviewPostDto") InterviewDto interviewPostDto, BindingResult bindingResult, Model model){
        interviewValidator.validate(interviewPostDto, bindingResult);
        if(bindingResult.hasErrors()) {
            List<Job> jobList = interviewService.getAllJob();
            model.addAttribute("jobsList", jobList);
            List<Candidate> candidateList = interviewService.getAllCandidate();
            model.addAttribute("candidateList", candidateList);
            List<UserEntity> userList = interviewService.getAllUser();
            model.addAttribute("userList", userList);
            List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();
            model.addAttribute("userInterviewerList", userInterviewerList);
            return "interviewCreate";
        }
        interviewService.createInterview(interviewPostDto);
        return "redirect:/interview";
    }


    @GetMapping
    public String getInterviews(Model model,  InterviewGetResponse interviewPageDto, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size , @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String status) {
        if ((keyword != null && !keyword.isEmpty()) || (status != null && !status.isEmpty())) {
            interviewPageDto = interviewService.searchInterviewer(keyword, status, page, size);
        } else {
            interviewPageDto = interviewService.findAll(page, size);
        }
        model.addAttribute("interviewLists", interviewPageDto.getInterviewResponseList());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", interviewPageDto.getTotalPage());
        model.addAttribute("totalElement", interviewPageDto.getTotalElements());
        return "interview";
    }

    @GetMapping("/ViewInterview/{id}")
    public String getInterviewDetail(@PathVariable Integer id, Model model) {
        InterviewGetResponse.InterviewResponse viewInterviewDto = interviewService.getInterviewById(id);
        model.addAttribute("viewInterviewDto", viewInterviewDto);
        return "interviewView";
    }

    @GetMapping("/showEdit/{id}")
    public String showEdit(@PathVariable(value = "id")  Integer id, Model model) {
        InterviewDto edit = interviewService.findById(id);
        model.addAttribute("edit", edit);
        List<Job> jobList = interviewService.getAllJob();
        model.addAttribute("jobsList", jobList);
        List<Candidate> candidateList = interviewService.getAllCandidate();
        model.addAttribute("candidateList", candidateList);
        List<UserEntity> userList = interviewService.getAllUser();
        model.addAttribute("userList", userList);
        List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();
        model.addAttribute("userInterviewerList", userInterviewerList);
        return "interviewEdit";
    }
    @PostMapping("/edit")
    public String edit(@ModelAttribute("edit") InterviewDto interviewPostDto, BindingResult bindingResult, Model model)  throws Exception {
        interviewValidator.validate(interviewPostDto, bindingResult);
        if(bindingResult.hasErrors()) {
            List<Job> jobList = interviewService.getAllJob();
            model.addAttribute("jobsList", jobList);
            List<Candidate> candidateList = interviewService.getAllCandidate();
            model.addAttribute("candidateList", candidateList);
            List<UserEntity> userList = interviewService.getAllUser();
            model.addAttribute("userList", userList);
            List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();
            model.addAttribute("userInterviewerList", userInterviewerList);
            return "interviewEdit";
        }
        InterviewDto interviewPostDto1 = new InterviewDto();
        interviewPostDto1.setInterviewId(interviewPostDto.getInterviewId());
        interviewPostDto1.setScheduleTitle(interviewPostDto.getScheduleTitle());
        interviewPostDto1.setCandidateName(interviewPostDto.getCandidateName());
        interviewPostDto1.setScheduleTime(interviewPostDto.getScheduleTime());
        interviewPostDto1.setScheduleFrom(interviewPostDto.getScheduleFrom());
        interviewPostDto1.setScheduleTo(interviewPostDto.getScheduleTo());
        interviewPostDto1.setLocation(interviewPostDto.getLocation());
        interviewPostDto1.setJob(interviewPostDto.getJob());
        interviewPostDto1.setInterview(interviewPostDto.getInterview());
        interviewPostDto1.setRecruiterOwner(interviewPostDto.getRecruiterOwner());
        interviewPostDto1.setMeetingID(interviewPostDto.getMeetingID());
        interviewPostDto1.setNotes(interviewPostDto.getNotes());
        interviewPostDto1.setStatus(interviewPostDto.getStatus());
        interviewPostDto1.setResult(interviewPostDto.getResult());
        interviewService.EditInterview(interviewPostDto1, interviewPostDto.getInterviewId());
        return "redirect:/interview";
    }
    @PostMapping("/cancelSchedule")
    public String cancelSchedule(@RequestParam("id")  Integer id) {
        interviewService.cancelSchedule(id);
        return "redirect:/interview"; // Redirect to the interview page or another page after cancelling
    }
}
