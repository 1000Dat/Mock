package fa.training.interviewmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer interviewId;
    private String scheduleTitle;
    private LocalDate scheduleTime;
    private LocalTime scheduleFrom;
    private LocalTime scheduleTo;
    private String location;
    private String meetingId;
    private String notes;
    private String result;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candId")
    private Candidate candidate;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL)
    private List<Offer> offerList;

    @ManyToMany
    @JoinTable(
            name = "user_interview",
            joinColumns = @JoinColumn(name = "interviewId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<UserEntity> interviewers;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", referencedColumnName = "userId")
    private UserEntity recruiter;

}
