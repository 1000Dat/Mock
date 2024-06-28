package fa.training.interviewmanagement.model.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import java.time.LocalDate;
import java.util.List;

@Data
public class JobDto {
    private Integer jobId;

    @NotBlank
    @NotBlank(message = "Bạn chưa nhập" )
    @Length(min = 1, max = 100)
    private String title;
    @NotNull(message = "Bạn chưa nhập")
    private LocalDate startWork;
    @NotNull(message = "Bạn chưa nhập")
    private LocalDate endWork;
    @Length(min = 1, max = 100)
    @NotNull(message = "Bạn chưa nhập")
    private String address;
    @Length(min = 1, max = 100)
    @NotNull(message = "Bạn chưa nhập")
    private String description;
    @NotNull(message = "Bạn chưa nhập")
    private String fr;
    @NotNull(message = "Bạn chưa nhập")
    private String t;
    @NotNull(message = "Bạn chưa nhập")
    private List<String> skill;
    @NotNull(message = "Bạn chưa nhập")
    private List<String> benefits;
    @NotNull(message = "Bạn chưa nhập")
    private List<String> level;
    private StatusJobEnum status;
}
