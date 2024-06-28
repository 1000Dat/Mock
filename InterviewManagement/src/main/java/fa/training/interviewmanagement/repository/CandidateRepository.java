package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entity.Candidate;
import fa.training.interviewmanagement.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    @Query("SELECT c FROM Candidate c WHERE c.status = 'Open'")
    List<Candidate> findAllOpen();
    Candidate findFirstByName(String name);
    Page<Candidate> findByNameContaining(String candidateName, Pageable pageable);
    Page<Candidate> findByEmailContaining(String candidateEmail, Pageable pageable);
    Page<Candidate> findByPhoneContaining(String candidatePhone, Pageable pageable);
    Page<Candidate> findByOwnerHRContaining(String ownerHR, Pageable pageable);

    @Query("SELECT c FROM Candidate c WHERE LOWER(c.status) LIKE LOWER(CONCAT('%', :status, '%'))")
    Page<Candidate> findByStatusContaining(@Param("status") String position, Pageable pageable);

    @Query("SELECT c FROM Candidate c WHERE LOWER(c.currentPosition) LIKE LOWER(CONCAT('%', :position, '%'))")
    Page<Candidate> findByCurrentPositionContaining(@Param("position") String position, Pageable pageable);
}
