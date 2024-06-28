package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UploadHistoryEntity;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.job.JobDto;
import fa.training.interviewmanagement.model.job.JobGetResponse;
import fa.training.interviewmanagement.model.job.StatusUploadHistoryEnum;
import fa.training.interviewmanagement.repository.UploadHistoryRepository;
import fa.training.interviewmanagement.repository.JobRepositoryCustom;
import fa.training.interviewmanagement.repository.UserRepository;
import fa.training.interviewmanagement.service.ExcelService;
import fa.training.interviewmanagement.service.JobService;
import fa.training.interviewmanagement.service.processor.JobValidateProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
public class JobController {
    @Autowired
    JobService jobService;
    @Autowired
    JobValidateProcessor validateProcessor;
    @Autowired
    JobRepositoryCustom jobCustom;
    @Autowired
    private ExcelService excelService;

    @Autowired
    private UploadHistoryRepository uploadHistoryRepository;
    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/job")
//    public String job(Model model, @RequestParam(defaultValue = "0") Integer page,
//                      @RequestParam(defaultValue = "9") Integer size,
//                      @RequestParam(value = "key", required = false) String key,
//                      @RequestParam(value = "optionSearch", required = false) String optionSearch, HttpServletRequest request,
//                      Principal principal) {
//        if (key != null && !key.isEmpty()) {
//            log.info("XXX optionSearch: {}", optionSearch);
//            // Search functionality
//            List<Job> list = jobCustom.searchJob(key, optionSearch);
//            model.addAttribute("List", list);
//            model.addAttribute("searchKey", key); // Add the search key to the model to retain the search term in the input field
//            // TODO
//        } else {
//            JobGetResponse certGetResponse = jobService.findAll(page, size);
//            model.addAttribute("List", certGetResponse.getEntityList());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", certGetResponse.getTotalPage());
//            model.addAttribute("totalElements", certGetResponse.getTotalElements());
//        }
//
//        UserEntity userEntity = checkLogin(principal);
//        if (userEntity == null) {
//            return "redirect:/login";
//        }
//        boolean isDisplayDownload = true;
//        List<UploadHistoryEntity> uploadHistoryEntities = uploadHistoryRepository.findByStatusAndUserId(StatusUploadHistoryEnum.FAILURE, userEntity);
//        if(uploadHistoryEntities.isEmpty()) {
//             isDisplayDownload = false;
//
//        }
//        model.addAttribute("isDisplayDownload", isDisplayDownload);
//
//
//
//        Boolean newFileUploaded = (Boolean) request.getSession().getAttribute("newFileUploaded");
//        model.addAttribute("newFileUploaded", newFileUploaded != null && newFileUploaded);
//        return "jobList";
//    }


    @GetMapping("/job")
    public String getUser(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleName = "";
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                roleName = authority.getAuthority(); // Return the first role name
            }
        }
        if(roleName.equalsIgnoreCase("ROLE_ADMIN")){
            return "redirect:/jobList";
        } else {
            return "redirect:/jobForUser";
        }
    }

    @GetMapping("/jobList")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN', 'MANAGER')")
    public String jobList(Model model, @RequestParam(defaultValue = "0") Integer page,
                          @RequestParam(defaultValue = "9") Integer size,
                          @RequestParam(value = "key", required = false) String key,
                          @RequestParam(value = "optionSearch", required = false) String optionSearch, HttpServletRequest request,
                          Principal principal) {
        if (key != null && !key.isEmpty()) {
            log.info("XXX optionSearch: {}", optionSearch);
            // Search functionality
            List<Job> list = jobCustom.searchJob(key, optionSearch);
            model.addAttribute("List", list);
            model.addAttribute("searchKey", key); // Add the search key to the model to retain the search term in the input field
            // TODO
        } else {
            JobGetResponse certGetResponse = jobService.findAll(page, size);
            model.addAttribute("List", certGetResponse.getEntityList());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", certGetResponse.getTotalPage());
            model.addAttribute("totalElements", certGetResponse.getTotalElements());
        }

        UserEntity userEntity = checkLogin(principal);
        if (userEntity == null) {
            return "redirect:/login";
        }
        boolean isDisplayDownload = true;
        List<UploadHistoryEntity> uploadHistoryEntities = uploadHistoryRepository.findByStatusAndUserId(StatusUploadHistoryEnum.FAILURE, userEntity);
        if(uploadHistoryEntities.isEmpty()) {
            isDisplayDownload = false;

        }
        model.addAttribute("isDisplayDownload", isDisplayDownload);

        Boolean newFileUploaded = (Boolean) request.getSession().getAttribute("newFileUploaded");
        model.addAttribute("newFileUploaded", newFileUploaded != null && newFileUploaded);
        return "jobList";
    }
    @GetMapping("/jobForUser")
    @PreAuthorize("hasAnyRole('INTERVIEWER')")
    public String jobForUser(Model model, @RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "9") Integer size,
                             @RequestParam(value = "key", required = false) String key,

                             @RequestParam(value = "optionSearch", required = false) String optionSearch, HttpServletRequest request,
                             Principal principal) {
        if (key != null && !key.isEmpty()) {
            log.info("XXX optionSearch: {}", optionSearch);
            // Search functionality
            List<Job> list = jobCustom.searchJob(key, optionSearch);
            model.addAttribute("List", list);
            model.addAttribute("searchKey", key); // Add the search key to the model to retain the search term in the input field
            // TODO
        } else {
            JobGetResponse certGetResponse = jobService.findAll(page, size);
            model.addAttribute("List", certGetResponse.getEntityList());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", certGetResponse.getTotalPage());
            model.addAttribute("totalElements", certGetResponse.getTotalElements());
        }

        UserEntity userEntity = checkLogin(principal);
        if (userEntity == null) {
            return "redirect:/login";
        }
        return "jobForUser";
    }


    @GetMapping("/add-job")
    public String jobAdd(Model model, JobDto jobDto) {
        // Đưa jobDto vào model để hiển thị trên view
        model.addAttribute("jobcreate", jobDto);

        // Ví dụ, bạn có thể lấy các giá trị mặc định trực tiếp từ jobDto nếu đã được set từ job
        model.addAttribute("skill", jobDto.getSkill());
        model.addAttribute("benefits", jobDto.getBenefits());
        model.addAttribute("levels", jobDto.getLevel());
        return "jobCreate";
    }

    @PostMapping("/delete-job/{id}")
    public String deleteJob(@PathVariable("id") Integer id, Model model) {
        try {
            jobService.delete(id);
            model.addAttribute("success", "Job deleted successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting job: " + e.getMessage());
        }
        return "redirect:/job";
    }


    @GetMapping("/job-details/{jobId}")
    public String jobDetails(@PathVariable Integer jobId, Model model) {
        try {
            Job job = jobService.findById(jobId);
            model.addAttribute("job", job);
            return "jobDetail";
        } catch (RuntimeException e) {

            return "jobNotFound"; // For example, return a separate error page
        }

    }

    @GetMapping("/job-details-interviewer/{jobId}")
    public String jobDetailsInterviewer(@PathVariable Integer jobId, Model model) {
        try {
            Job job = jobService.findById(jobId);
            model.addAttribute("job", job);
            return "jobDetails_interviewer";
        } catch (RuntimeException e) {

            return "jobNotFound"; // For example, return a separate error page
        }
    }

    @GetMapping("/job-edit/{jobId}")
    public String jobEdit(@PathVariable Integer jobId, Model model) {
        try {
            Job job = jobService.findById(jobId);

            // Map từ đối tượng Job sang JobDto
            JobDto jobDto = new JobDto();
            jobDto.setJobId(job.getJobId());
            jobDto.setAddress(job.getAddress());
            jobDto.setTitle(job.getTitle());
            jobDto.setStartWork(job.getStartWork());
            jobDto.setEndWork(job.getEndWork());
            jobDto.setDescription(job.getDescription());
            jobDto.setFr(job.getFr());
            jobDto.setT(job.getT());
            jobDto.setSkill(Arrays.stream(job.getSkill().split(",")).toList());
            jobDto.setBenefits(Arrays.stream(job.getBenefits().split(",")).toList());
            jobDto.setLevel(Arrays.stream(job.getLevel().split(",")).toList());
            jobDto.setStatus(job.getStatus());

            // Đưa jobDto vào model để hiển thị trên view
            model.addAttribute("jobDto", jobDto);

            // Ví dụ, bạn có thể lấy các giá trị mặc định trực tiếp từ jobDto nếu đã được set từ job
            model.addAttribute("skill", jobDto.getSkill());
            model.addAttribute("benefits", jobDto.getBenefits());
            model.addAttribute("levels", jobDto.getLevel());

            return "jobEdit"; // Trả về tên view template (jobEdit.html)
        } catch (RuntimeException e) {
            return "jobNotFound"; // Xử lý ngoại lệ, ví dụ trả về trang lỗi
        }
    }

    @PostMapping("/update-job")
    public String saveJob(@ModelAttribute("jobDto") @Valid JobDto jobDto, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        validateProcessor.validateTitle(jobDto,result);
        validateProcessor.validateScheduleTime(jobDto, result);
        if (validateProcessor.isValidate(result, jobDto).hasErrors()) {
            // Đưa jobDto vào model để hiển thị trên view
            model.addAttribute("jobDto", jobDto);

            // Thêm các attribute cho view
            model.addAttribute("skill", jobDto.getSkill());
            model.addAttribute("benefits", jobDto.getBenefits());
            model.addAttribute("levels", jobDto.getLevel());

            return "jobEdit"; // Trả về trang biểu mẫu thực tế
        }

        jobService.updateJob(jobDto.getJobId(), jobDto);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công: " + jobDto.getTitle());
        return "redirect:/job";
    }


    @PostMapping("/files/upload")
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Principal principal) throws IOException {
       UserEntity userEntity = checkLogin(principal);
        if (userEntity == null) {
            return "redirect:/login";
        }

        excelService.save(file, userEntity);
        redirectAttributes.addFlashAttribute("message", "Upload thành công ");
        return "redirect:/job";
    }

    @GetMapping("/files/download")
    public void downloadEditedFile(HttpServletResponse response) {

        File editFile = excelService.getEditedFile();
        if (editFile.exists()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=edited_template.xlsx");

            try (InputStream inputStream = new FileInputStream(editFile);
                 OutputStream outputStream = response.getOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private UserEntity checkLogin(Principal principal) {
        if (principal == null) {
            return null;
        }
        Optional<UserEntity> userEntityOptional = userRepository.findUserEntityByEmail(principal.getName());
        return userEntityOptional.orElse(null);
    }
}
