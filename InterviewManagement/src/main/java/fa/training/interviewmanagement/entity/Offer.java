package fa.training.interviewmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(schema = "mock")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer offerId;
    private String position;
    private LocalDate contactFrom;
    private LocalDate contactTo;
    private String contactType;
    private String level;
    private String department;
    private LocalDate dueDate;
    private Double salary;
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewId")
    private Interview interview;
}
