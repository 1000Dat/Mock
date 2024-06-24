package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {
    @Query("SELECT i FROM Interview i " +
            "WHERE LOWER(CONCAT(i.result, i.candidate.name, i.job.title, i.scheduleTitle,i.scheduleFrom, i.scheduleTo, i.status)) LIKE %:keyword% " +
            "AND LOWER(i.status) LIKE %:status%")
    Page<Interview> findByKeywordAndStatusContainingIgnoreCase(
            @Param("keyword") String keyword,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT i FROM Interview i " +
            "WHERE LOWER(CONCAT(i.result, i.candidate.name, i.job.title, i.scheduleTitle,i.scheduleFrom, i.scheduleTo, i.status)) LIKE %:keyword%")
    Page<Interview> findByKeywordInAnyFieldIgnoreCase(
            @Param("keyword") String keyword,
            Pageable pageable);


    Page<Interview> findByStatusContainingIgnoreCase(String status, Pageable pageable);
}
