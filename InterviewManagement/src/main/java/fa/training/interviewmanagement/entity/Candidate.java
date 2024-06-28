package fa.training.interviewmanagement.entity;

import fa.training.interviewmanagement.model.candidate.CandidateEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer candId;
    private String name;
    private String email;
    private LocalDate dob;
    private String address;

    @Enumerated(EnumType.STRING)
    private CandidateEnum.CandidateGender gender;

    private String phone;
    private String professionalInformation;

    @Enumerated(EnumType.STRING)
    private CandidateEnum.CurrentPosition currentPosition;

    private CandidateEnum.CandidateSkill skill;
    private CandidateEnum.CandidateHighestLevel highestLevel;
    private String ownerHR;

    @Enumerated(EnumType.STRING)
    private CandidateEnum.CandidateStatus status;

    private String note;
    private Integer yearOfExperience;
    private String CandidateCV;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Interview> interviewList;
}
