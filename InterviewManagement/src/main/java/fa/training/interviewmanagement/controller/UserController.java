package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.user.UserDto;
import fa.training.interviewmanagement.model.user.UserGetResponse;
import fa.training.interviewmanagement.repository.UserRepositoryCustom;
import fa.training.interviewmanagement.service.UserService;
import fa.training.interviewmanagement.service.processor.UserValidateProcessor;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepositoryCustom custom;
    @Autowired
    UserValidateProcessor validateProcessor;

    @GetMapping("/homepage")
    @PreAuthorize("hasAnyRole('ADMIN','RECRUITER', 'INTERVIEWER', 'MANAGER')")
    public String homePage(){
        return "home";
    }

    @GetMapping("/userList")
    public String userList(Model model, @RequestParam(defaultValue = "0") Integer page,
                           @RequestParam(defaultValue = "2") Integer size,
                           @RequestParam(value = "key", required = false) String key,
                           @RequestParam(value = "optionSearch", required = false) String optionSearch){
        if (key != null && !key.isEmpty()) {
            log.info("XXX optionSearch: {}", optionSearch);
            // Search functionality
            List<UserEntity> list = custom.searchUser(key, optionSearch);
            model.addAttribute("list", list);
            model.addAttribute("searchKey", key);
        } else {
            UserGetResponse userGetResponse = userService.findAll(page, size);
            model.addAttribute("list", userGetResponse.getUserResponseList());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userGetResponse.getTotalPage());
            model.addAttribute("totalElements", userGetResponse.getTotalElements());
        }
        return "userList";
    }

    @PostMapping("/create-user")
    public String createUser(@ModelAttribute("userCreate") @Valid UserDto userDto, BindingResult result, Model model){
        validateProcessor.validateEmail(userDto,result);
        validateProcessor.validateDob(userDto, result);
        if (result.hasErrors()) {
            model.addAttribute("userCreate", userDto);
            return "userCreate";
        }
        userService.createUser(userDto);
        return "redirect:/userList";
    }

    @GetMapping("/add")
    public String addUser(Model model){
        model.addAttribute("userCreate", new UserDto());
        return "userCreate";
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable(value = "id") Integer id){
        userService.deleteUser(id);
        return "redirect:/userList";
    }

    @GetMapping("/detailUser")
    public String detailUser(Model model){
        return "userDetail";
    }

    @GetMapping("/user-detail/{userId}")
    public String detailUser(@PathVariable Integer userId, Model model){
        try{
            UserEntity userEntity = userService.findById(userId);
            model.addAttribute("user", userEntity);
            return "userDetail";
        } catch (RuntimeException e){
            return "userNotFound";
        }
    }

    @GetMapping("/user-edit/{userId}")
    public String userEdit(@PathVariable Integer userId, Model model){
        try{
            UserEntity userEntity = userService.findById(userId);
            UserDto userDto = new UserDto();
            userDto.setUserId(userEntity.getUserId());
            userDto.setUsername(userEntity.getUsername());
            userDto.setRole(userEntity.getRole());
            userDto.setDob(userEntity.getDob());
            userDto.setPhone(userEntity.getPhone());
            userDto.setEmail(userEntity.getEmail());
            userDto.setAddress(userEntity.getAddress());
            userDto.setGender(userEntity.getGender());
            userDto.setStatus(userEntity.getStatus());
            userDto.setNote(userEntity.getNote());
            model.addAttribute("userDto", userDto);
            return "userEdit";
        } catch (RuntimeException e){
            return "userNotFound";
        }
    }

    @PostMapping("/update-user")
    public String saveUser(@ModelAttribute UserDto userDto){
        userService.updateUser(userDto.getUserId(), userDto);
        return "redirect:/userList";
    }
}
