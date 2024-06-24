package fa.training.interviewmanagement.service.processor;

import fa.training.interviewmanagement.model.user.UserDto;
import fa.training.interviewmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

@Component
public class UserValidateProcessor {
    @Autowired
    UserRepository userRepository;

    public  void validateDob(UserDto userDto, BindingResult bindingResult) {
        if (userDto.getDob() == null || userDto.getDob().isAfter(LocalDate.now())) {
            bindingResult.rejectValue("dob", "error.dob", "Ngày sinh không được là ngày trong tương lai");
        }
    }
    public boolean checkDuplicateEmail(String email) {
        int emailDuplicateCount = userRepository.countByEmail(email);
        return emailDuplicateCount > 0;
    }
    public void validateEmail(UserDto userDto, BindingResult bindingResult) {
        if (checkDuplicateEmail(userDto.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email already exists");
        }

    }
}
