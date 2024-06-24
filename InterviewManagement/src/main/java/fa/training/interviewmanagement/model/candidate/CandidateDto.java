package fa.training.interviewmanagement.model.candidate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CandidateDto {
    private Integer candId;
    @NotBlank(message = "Name problem")
    @Pattern(regexp = "^[a-zA-z\s]{6,}", message = "name must atleast have 6 characters or more and did not include special character ")
    private String name;
    @NotNull
    @NotBlank(message = "email problem")
    @Email(message = "Email is invalid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @Enumerated(EnumType.STRING)
    private CandidateEnum.CandidateGender gender;
    private LocalDate dob;
    private String address;
    @NotNull
    @NotEmpty(message = "phone problem")
    @Pattern(regexp = "^[0-9]{10,11}", message = "phone number only accept numbers and need 10-11 numbers for phone number")
    private String phone;
    private String professionalInformation;
    @Enumerated(EnumType.STRING)
    private CandidateEnum.CurrentPosition currentPosition;
    private CandidateEnum.CandidateSkill skill;
    @Enumerated(EnumType.STRING)
    private CandidateEnum.CandidateHighestLevel highestLevel;
    private String ownerHR;
    @Enumerated(EnumType.STRING)
    private CandidateEnum.CandidateStatus status;
    private String note;
    private Integer yearOfExperience;
    private String CandidateCV;
}
