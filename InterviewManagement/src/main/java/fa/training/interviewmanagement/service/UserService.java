package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.user.LoginUser;
import fa.training.interviewmanagement.model.user.UserDto;
import fa.training.interviewmanagement.model.user.UserGetResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createUser(UserDto userDto);
    UserGetResponse findAll(int page, int size);
    void deleteUser(Integer id);
    UserEntity findById(Integer id);
    List<UserEntity> searchUser(String key);
    UserEntity updateUser(Integer id, UserDto userDto);
    UserEntity login(LoginUser loginUser);

    UserEntity logout(LoginUser loginUser);
    Optional<UserEntity> findByEmail(String email);
    UserEntity updatePass(UserDto userDto, String email);
}
