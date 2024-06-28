package fa.training.interviewmanagement.entity;

import fa.training.interviewmanagement.model.job.StatusUploadHistoryEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class UploadHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime uploadDate;
    @Enumerated(EnumType.STRING)
    private StatusUploadHistoryEnum statusUploadHistoryEnum;

    @ManyToOne
    @JoinColumn(name = "uploadBy", referencedColumnName = "userId")
    private UserEntity uploadBy;
}
