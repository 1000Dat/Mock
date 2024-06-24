package fa.training.interviewmanagement.model.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetResponse {
    private List<UserResponse> userResponseList;
    private Integer totalPage;
    private Integer totalElements;

    @Data
    public static class UserResponse {
        Integer userId;
        String username;
        String email;
        String phone;
        String role;
        String status;
    }
}
