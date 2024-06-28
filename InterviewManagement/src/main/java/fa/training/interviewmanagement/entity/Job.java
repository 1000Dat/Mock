package fa.training.interviewmanagement.entity;

import fa.training.interviewmanagement.model.job.StatusJobEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(unique = true)
    private String title;
    private LocalDate startWork;
    private LocalDate endWork;
    private String fr;
    private String t;
    private String skill;
    private String benefits;
    String level;

    @Enumerated(EnumType.STRING)
    private StatusJobEnum status;

    private String address;
    private String description;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Interview> interviewList;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Offer> offerList;
}
