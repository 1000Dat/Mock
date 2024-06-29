package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.interview.InterviewCandidate;
import fa.training.interviewmanagement.model.interview.InterviewDto;
import fa.training.interviewmanagement.model.interview.InterviewEditDto;
import fa.training.interviewmanagement.model.interview.InterviewGetResponse;
import fa.training.interviewmanagement.service.InterviewService;
import fa.training.interviewmanagement.service.processor.InterviewValidateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private InterviewValidateProcessor interviewValidator;

    @GetMapping("/showCreate")
    public String showAddInterview(Model model) {
        InterviewDto interviewPostDto = new InterviewDto();
        model.addAttribute("interviewPostDto",  interviewPostDto);
        List<Job> jobList = interviewService.getAllJob();
        model.addAttribute("jobsList", jobList);
        List<Candidate> candidateList = interviewService.getAllCandidate();
        model.addAttribute("candidateList", candidateList);
        List<UserEntity> userList = interviewService.getAllUser();
        model.addAttribute("userList", userList);
        List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();
        model.addAttribute("userInterviewerList", userInterviewerList);

        List<InterviewCandidate> interviewCandidates = candidateList.stream()
                .map(c -> new InterviewCandidate(c.getCandId(), c.getName(),c.getEmail()))
                .collect(Collectors.toList());

        interviewPostDto.setCandidates(interviewCandidates);

        return "interviewCreate";
    }

    @PostMapping("/createInterview")
    public String createInterview(@ModelAttribute("interviewPostDto") InterviewDto interviewPostDto, BindingResult bindingResult, Model model){
        interviewValidator.validate(interviewPostDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Job> jobList = interviewService.getAllJob();
            List<Candidate> candidateList = interviewService.getAllCandidate();
            List<InterviewCandidate> interviewCandidates = candidateList.stream()
                    .map(c -> new InterviewCandidate(c.getCandId(), c.getName(),c.getEmail()))
                    .collect(Collectors.toList());
            interviewPostDto.setCandidates(interviewCandidates);
            List<UserEntity> userList = interviewService.getAllUser();
            List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();

            model.addAttribute("jobsList", jobList);
            model.addAttribute("candidateList", candidateList);
            model.addAttribute("userList", userList);
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

    @GetMapping("/interviewer")
    public String getInterviewer(Model model,  InterviewGetResponse interviewPageDto, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size , @RequestParam(required = false) String keyword,
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
        return "interviewer";
    }

    @GetMapping("/ViewInterview/{id}")
    public String getInterviewDetail(@PathVariable Integer id, Model model) {
        InterviewGetResponse.InterviewResponse viewInterviewDto = interviewService.getInterviewById(id);
        model.addAttribute("viewInterviewDto", viewInterviewDto);
        return "interviewView";
    }

    @GetMapping("/ViewInterviewer/{id}")
    public String getInterviewerDetail(@PathVariable Integer id, Model model) {
        InterviewGetResponse.InterviewResponse viewInterviewDto = interviewService.getInterviewById(id);
        model.addAttribute("viewInterviewDto", viewInterviewDto);
        return "interviewerView";
    }

    @GetMapping("/showEdit/{id}")
    public String showEdit(@PathVariable(value = "id")  Integer id, Model model) {
        InterviewEditDto edit = interviewService.findById(id);
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
    public String edit(@ModelAttribute("edit") InterviewEditDto interviewPostDto, BindingResult bindingResult, Model model)  throws Exception {
//        interviewValidator.validate(interviewPostDto, bindingResult);

            if (bindingResult.hasErrors()) {
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

            interviewService.EditInterview(interviewPostDto, interviewPostDto.getId());

        return "redirect:/interview";


    }
    @PostMapping("/cancelSchedule")
    public String cancelSchedule(@RequestParam("id")  Integer id) {
        interviewService.cancelSchedule(id);
        return "redirect:/interview"; // Redirect to the interview page or another page after cancelling
    }



    @GetMapping("/interviewer/showSubmit/{id}")
    public String showSubmit(@PathVariable(value = "id")  Integer id, Model model ) {
        InterviewEditDto submit = interviewService.findById(id);
        model.addAttribute("submit",submit);
        List<Job> jobList = interviewService.getAllJob();
        model.addAttribute("jobsList", jobList);
        List<Candidate> candidateList = interviewService.getAllCandidate();
        model.addAttribute("candidateList", candidateList);
        List<UserEntity> userList = interviewService.getAllUser();
        model.addAttribute("userList", userList);
        List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();
        model.addAttribute("userInterviewerList", userInterviewerList);
        model.addAttribute("username");
        return "interviewerSubmit";
    }
    @PostMapping("/interviewer/submitEdit")
    public String submit(@ModelAttribute("submit") InterviewEditDto interviewPostDto, BindingResult bindingResult, Model model)  throws Exception {
        if(bindingResult.hasErrors()) {
            List<Job> jobList = interviewService.getAllJob();
            model.addAttribute("jobsList", jobList);
            List<Candidate> candidateList = interviewService.getAllCandidate();
            model.addAttribute("candidateList", candidateList);
            List<UserEntity> userList = interviewService.getAllUser();
            model.addAttribute("userList", userList);
            List<UserEntity> userInterviewerList = interviewService.getAllUserInterviewer();
            model.addAttribute("userInterviewerList", userInterviewerList);
            return "interviewerSubmit";
        }
        InterviewDto interviewPostDto1 = new InterviewDto();
        interviewPostDto1.setInterviewId(interviewPostDto.getId());
        interviewPostDto1.setNotes(interviewPostDto.getNotes());
        interviewPostDto1.setResult(interviewPostDto.getResult());
        interviewPostDto1.setStatus(interviewPostDto.getStatus());
        interviewService.Interviewed(interviewPostDto1, interviewPostDto.getId());

        return "redirect:/interview/interviewer" ;
    }
}
