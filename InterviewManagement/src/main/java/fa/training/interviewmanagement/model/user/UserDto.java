package fa.training.interviewmanagement.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Integer userId;

    @NotBlank(message = "Bạn chưa nhập")
    private String username;
    private String role;
    @NotNull(message = "Bạn chưa nhập")
    private LocalDate dob;
    @NotBlank(message = "Bạn chưa nhập")
    private String phone;
    @NotBlank(message = "Bạn chưa nhập")
    private String email;
    @NotBlank(message = "Bạn chưa nhập")
    private String address;
    private String gender;
    private String department;
    private String status;
    @NotBlank(message = "Bạn chưa nhập")
    private String note;
}
