package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String name);
    int countByEmail(String email);
    @Query("SELECT u FROM UserEntity u WHERE u.role = 'Recruiter'")
    List<UserEntity> findRecruiter();
    Optional<UserEntity> findUserEntityByEmail(String email);
    UserEntity findUserEntityByStatus(String status);
    @Query("SELECT u FROM UserEntity u WHERE u.role = 'Recruiter'")
    List<UserEntity> findAllRecruiters();
    @Query("SELECT u FROM UserEntity u WHERE u.role = 'Interviewer'")
    List<UserEntity> findAllInterviewers();
}
