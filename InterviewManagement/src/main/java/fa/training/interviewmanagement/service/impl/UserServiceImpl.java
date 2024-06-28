package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.customError.PasswordMismatchException;
import fa.training.interviewmanagement.customError.UserNotValidException;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.user.LoginUser;
import fa.training.interviewmanagement.model.user.UserDto;
import fa.training.interviewmanagement.model.user.UserGetResponse;
import fa.training.interviewmanagement.repository.UserRepository;
import fa.training.interviewmanagement.service.MailService;
import fa.training.interviewmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setRole(userDto.getRole());
        userEntity.setDob(userDto.getDob());
        userEntity.setPhone(userDto.getPhone());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setAddress(userDto.getAddress());
        userEntity.setGender(userDto.getGender());
        userEntity.setStatus(userDto.getStatus());
        userEntity.setNote(passwordEncoder.encode(userDto.getNote()));
        userRepository.save(userEntity);
        mailService.sendEmail(userDto.getEmail(), "no-reply-email-IMS-system <Account created>",
                "This email is from IMS system\n"
                        + "Your account has been created. Please use the following credential to login:\n"
                        + "User name:" + userDto.getEmail()
                        + "\nPassword:" + userDto.getNote()
                        + "\nIf anything wrong, please reach-out recruiter < offer recruiter owner account>. We are so sorry for this inconvenience\n"
                        + "Thanks & Regards!\n"
                        + "IMS Team.”");
    }

    @Override
    public UserGetResponse findAll(int page, int size) {
        UserGetResponse userGetResponse = new UserGetResponse();
        Page<UserEntity> userEntityPage = userRepository.findAll(PageRequest.of(page, size));
        List<UserGetResponse.UserResponse> userResponseList = userEntityPage
                .getContent()
                .stream()
                .map(user -> {
                    UserGetResponse.UserResponse userResponse = new UserGetResponse.UserResponse();
                    userResponse.setUserId(user.getUserId());
                    userResponse.setUsername(user.getUsername());
                    userResponse.setRole(user.getRole());
                    userResponse.setEmail(user.getEmail());
                    userResponse.setPhone(user.getPhone());
                    userResponse.setStatus(user.getStatus());
                    return userResponse;
        }).collect(Collectors.toList());
        userGetResponse.setUserResponseList(userResponseList);
        userGetResponse.setTotalPage(userEntityPage.getTotalPages());
        userGetResponse.setTotalElements((int) userEntityPage.getTotalElements());
        return userGetResponse;
    }

    @Override
    public UserEntity findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay"));
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findUserEntityByEmail(email);
    }

    @Override
    public UserEntity updatePass(UserDto userDto, String email) {
        Optional<UserEntity> optionalUser = userRepository.findUserEntityByEmail(email);
        if (optionalUser.isPresent()) {
            mailService.sendPasswordResetEmail(email);
            UserEntity userEntity = optionalUser.get();
            userEntity.setNote(userDto.getNote());
            return userRepository.save(userEntity);
        } else {
            throw new RuntimeException("User not found with email " + email);
        }
    }

    @Override
    public List<UserEntity> searchUser(String key) {
        return null;
    }

    @Override
    public UserEntity updateUser(Integer id, UserDto userDto) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();
            userEntity.setUsername(userDto.getUsername());
            userEntity.setRole(userDto.getRole());
            userEntity.setDob(userDto.getDob());
            userEntity.setPhone(userDto.getPhone());
            userEntity.setEmail(userDto.getEmail());
            userEntity.setAddress(userDto.getAddress());
            userEntity.setGender(userDto.getGender());
            userEntity.setStatus(userDto.getStatus());
            userEntity.setNote(userDto.getNote());
            return userRepository.save(userEntity);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity login(LoginUser loginUser) {
        Optional<UserEntity> userOptional = userRepository.findUserEntityByEmail(loginUser.getEmail());
        UserEntity user = userOptional.get();
        if (userOptional.isEmpty()) {
            throw new UserNotValidException("User is not valid");
        } else if (!loginUser.getNote().equals(userOptional.get().getNote())) {
            throw new PasswordMismatchException("Passwords do not match");
        } else if (!"active".equals(user.getStatus())) {
            throw new UserNotValidException("account is inactive");
        }
        UserEntity currentUser = new UserEntity();
        currentUser.setUsername(userOptional.get().getUsername());
        currentUser.setEmail(userOptional.get().getEmail());
        currentUser.setDob(userOptional.get().getDob());
        currentUser.setAddress(userOptional.get().getAddress());
        currentUser.setPhone(userOptional.get().getPhone());
        currentUser.setGender(userOptional.get().getGender());
        currentUser.setRole(userOptional.get().getRole());
        currentUser.setDepartment(userOptional.get().getDepartment());
        currentUser.setStatus(userOptional.get().getStatus());
        currentUser.setNote(userOptional.get().getNote());
        return currentUser;
    }

    @Override
    public UserEntity logout(LoginUser loginUser) {
        return null;
    }
}
