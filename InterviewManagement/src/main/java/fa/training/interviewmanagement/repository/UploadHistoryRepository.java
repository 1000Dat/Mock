package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entity.UploadHistoryEntity;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.job.StatusUploadHistoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadHistoryRepository extends JpaRepository<UploadHistoryEntity, Integer> {

    @Query(value = "select t from UploadHistoryEntity t where t.statusUploadHistoryEnum = :status and t.uploadBy = :id order by t.uploadDate desc")
    List<UploadHistoryEntity> findByStatusAndUserId(StatusUploadHistoryEnum status, UserEntity id);
}
