package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.candidate.CandidateDto;
import fa.training.interviewmanagement.model.candidate.CandidateGetResponse;
import fa.training.interviewmanagement.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/candidate")
    public String candidateList(Model model, @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(value = "key", required = false) String key,
                                @RequestParam(value = "optionSearch", required = false) String optionSearch) {
//        if (key != null && !key.isEmpty()) {
//            log.info("XXX optionSearch: {}", optionSearch);
//            // Search functionality
//            List<CandidateEntity> list = repositoryCustom.searchCandidate(key, optionSearch);
//            model.addAttribute("list", list);
//            model.addAttribute("searchKey", key);
//        } else {
        CandidateGetResponse candidateGetResponse = candidateService.findAll(page, size);
        model.addAttribute("list", candidateGetResponse.getCandidateResponsesList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidateGetResponse.getTotalPage());
        model.addAttribute("totalElements", candidateGetResponse.getTotalElements());
//        }
        return "candidateList";
    }

    @GetMapping("/delete-candidate/{id}")
    public String deleteCandidate(@PathVariable(value = "id") Integer id) {
        candidateService.deleteCandidate(id);
        return "redirect:/candidate";
    }

    @PostMapping("/create-candidate")
    public String createCandidate(@ModelAttribute("candidateCreate") @Valid CandidateDto candidateDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("candidateCreate", candidateDto);
            return "candidateCreate";
        }
        candidateService.createCandidate(candidateDto);
        return "redirect:/candidate";
    }

    @GetMapping("/add-candidate")
    public String addCandidate(Model model) {
        model.addAttribute("candidateCreate", new CandidateDto());
        List<UserEntity> userList = candidateService.getRecruiter();
        model.addAttribute("userList", userList);
        return "candidateCreate";
    }

    @GetMapping("/detailsCandidate")
    public String detailCandidate(Model model) {
        return "candidateDetails";
    }

    @GetMapping("/candidate-detail/{candidateID}")
    public String detailCandidate(@PathVariable Integer candidateID, Model model) {
        try {
            Candidate candidate = candidateService.findById(candidateID);
            model.addAttribute("candidate", candidate);
            return "candidateDetails";
        } catch (RuntimeException e) {
            return "candidatenNotFound";
        }
    }

    @GetMapping("/candidate-edit/{candidateId}")
    public String candidateEdit(@PathVariable Integer candidateId, Model model) {
        try {
            Candidate candidate = candidateService.findById(candidateId);
            CandidateDto candidateDto = new CandidateDto();
            candidateDto.setCandId(candidate.getCandId());
            candidateDto.setName(candidate.getName());
            candidateDto.setEmail(candidate.getEmail());
            candidateDto.setGender(candidate.getGender());
            candidateDto.setDob(candidate.getDob());
            candidateDto.setAddress(candidate.getAddress());
            candidateDto.setPhone(candidate.getPhone());
            candidateDto.setProfessionalInformation(candidate.getProfessionalInformation());
            candidateDto.setCurrentPosition(candidate.getCurrentPosition());
            candidateDto.setSkill(candidate.getSkill());
            candidateDto.setHighestLevel(candidate.getHighestLevel());
            candidateDto.setOwnerHR(candidate.getOwnerHR());
            candidateDto.setStatus(candidate.getStatus());
            candidateDto.setNote(candidate.getNote());
            candidateDto.setYearOfExperience(candidate.getYearOfExperience());
            candidateDto.setCandidateCV(candidate.getCandidateCV());
            model.addAttribute("candidateDto", candidateDto);
            return "candidateEdit";
        } catch (RuntimeException e) {
            return "candidateNotFound";
        }
    }

    @PostMapping("/update-candidate")
    public String saveCandidate(@ModelAttribute CandidateDto candidateDto) {
        candidateService.updateCandidate(candidateDto.getCandId(), candidateDto);
        return "redirect:/candidate";
    }

    @GetMapping("/search")
    public String searchCandidates(Model model, @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam String keyword,
                                   @RequestParam String field) {
        if (field == null || keyword == null) {
            return "candidateList";
        }
        CandidateGetResponse candidateGetResponse = candidateService.searchCandidates(field, keyword, page, size);
        model.addAttribute("list", candidateGetResponse.getCandidateResponsesList());

        return "candidateSearch";
    }
}
