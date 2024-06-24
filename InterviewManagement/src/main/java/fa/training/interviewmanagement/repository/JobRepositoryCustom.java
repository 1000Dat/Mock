package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entity.Job;
import fa.training.interviewmanagement.entity.UserEntity;
import fa.training.interviewmanagement.model.job.StatusJobEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Job> searchJob(String key, String optionSearch) {
        StringBuilder queryBuilder = new StringBuilder("SELECT j FROM Job j WHERE 1=1");

        if (key != null && !key.isEmpty() && "title".equalsIgnoreCase(optionSearch)) {
            queryBuilder.append(" AND (j.title LIKE :key)");
        }
        if (key != null && !key.isEmpty() && "status".equalsIgnoreCase(optionSearch)) {
            queryBuilder.append(" AND (j.status = :status)");
        }
        if (key != null && !key.isEmpty() && "skill".equalsIgnoreCase(optionSearch)) {
            queryBuilder.append(" AND (j.skill LIKE :key)");
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Job.class);

        if (key != null && !key.isEmpty() && !"status".equalsIgnoreCase(optionSearch) ) {
            query.setParameter("key", "%" + key + "%");
        }
        // Set parameter for status only if optionSearch is "status" and key is not null
        if ("status".equalsIgnoreCase(optionSearch) && key != null && !key.isEmpty()) {
            try {
                StatusJobEnum statusJobEnum = StatusJobEnum.valueOf(key.toUpperCase());
                query.setParameter("status", statusJobEnum);
            } catch (Exception e) {
                query.setParameter("status", StatusJobEnum.OPEN);
                // Handle invalid status enum value if needed
            }
        }
        return query.getResultList();
    }


}
