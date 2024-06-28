package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    Job findFirstByTitle(String title);
    void deleteById(Integer id);

    @Query("SELECT j FROM Job j WHERE " +
            "(j.title LIKE %:title%) OR " +
            "(j.status LIKE %:title%)")
    List<Job> searchJob(@Param("title") String key);

    int  countByTitle(String title);

    @Query("SELECT j FROM Job j WHERE j.status = 'Open'")
    List<Job> findTitlesByOpen();

    @Query("SELECT j FROM Job j WHERE j.endWork < :date")
    List<Job> findByEndWork(LocalDate date);

    @Query("SELECT j FROM Job j WHERE j.startWork >= :date")
    List<Job> findBystartWork(LocalDate date);
}
