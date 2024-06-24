package fa.training.interviewmanagement.repository;

import fa.training.interviewmanagement.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    public List<UserEntity> searchUser(String key, String optionSearch) {
        StringBuilder queryBuilder = new StringBuilder("SELECT u FROM UserEntity u WHERE 1=1");
        boolean isKeyNotEmpty = key != null && !key.isEmpty();
        if (key != null && !key.isEmpty() && "username".equalsIgnoreCase(optionSearch)) {
            queryBuilder.append(" AND (u.username LIKE :key)");
        }
        if (key != null && !key.isEmpty() && "status".equalsIgnoreCase(optionSearch)) {
            queryBuilder.append(" AND (u.status = :status)");
        }
        if (key != null && !key.isEmpty() && "role".equalsIgnoreCase(optionSearch)) {
            queryBuilder.append(" AND (u.role LIKE :key)");
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), UserEntity.class);

        if (isKeyNotEmpty) {
            if ("username".equalsIgnoreCase(optionSearch) || "role".equalsIgnoreCase(optionSearch)) {
                query.setParameter("key", "%" + key + "%");
            } else if ("status".equalsIgnoreCase(optionSearch)) {
                query.setParameter("status", key);
            }
        }
        return query.getResultList();
    }
}
