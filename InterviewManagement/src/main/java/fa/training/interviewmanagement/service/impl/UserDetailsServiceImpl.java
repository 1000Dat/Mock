package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.customError.UserNotValidException;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findUserEntityByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotValidException("User is not valid");
        }
//        else if (!loginUser.getPassword().equals(userOptional.get().getPassword())) {
//            throw new PasswordMismatchException("Passwords do not match");
//        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        SimpleGrantedAuthority simpleGranted = new SimpleGrantedAuthority("ROLE_" + userOptional.get().getRole().toUpperCase());
        authorities.add(simpleGranted);

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(userOptional.get().getNote())
                .authorities(authorities)
                .build();
    }
}
