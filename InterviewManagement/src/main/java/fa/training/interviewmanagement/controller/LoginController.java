package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.customError.PasswordMismatchException;
import fa.training.interviewmanagement.customError.UserNotValidException;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.user.LoginUser;
import fa.training.interviewmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginUser") LoginUser loginUser, BindingResult result,
                            HttpSession session) {
        try {
            UserEntity currentUser = userService.login(loginUser);
            session.setAttribute("currentUser", currentUser);
            return "redirect:/home";
        } catch (UserNotValidException e) {
            result.rejectValue("email", "error.loginUser", e.getMessage());
        } catch (PasswordMismatchException e) {
            result.rejectValue("note", "error.loginUser", e.getMessage());
        }
        return "login";
    }

    @GetMapping("/home")
    public String home(Principal principal) {
        return "home";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        // Thực hiện logic gửi email reset password
        model.addAttribute("message", "A password reset link has been sent to " + email);
        return "forgotPassword";
    }
}
