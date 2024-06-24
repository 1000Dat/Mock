package fa.training.interviewmanagement.model.user;

import fa.training.interviewmanagement.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser extends UserEntity {
    private String email;
    private String note;
}
