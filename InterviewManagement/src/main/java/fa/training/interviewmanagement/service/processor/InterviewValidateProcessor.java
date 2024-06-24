package fa.training.interviewmanagement.service.processor;

import fa.training.interviewmanagement.customError.ValidationMessages;
import fa.training.interviewmanagement.model.interview.InterviewDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class InterviewValidateProcessor implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return InterviewDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InterviewDto interviewDto = (InterviewDto) target;

        // Validate scheduleTitle
        if (interviewDto.getScheduleTitle() == null || interviewDto.getScheduleTitle().isEmpty()) {
            errors.rejectValue("scheduleTitle", "error.scheduleTitle.notBlank",
                    ValidationMessages.SCHEDULE_TITLE_NOT_BLANK);
        }

        // Validate candidateName
        if (interviewDto.getCandidateName() == null || interviewDto.getCandidateName().isEmpty()) {
            errors.rejectValue("candidateName", "error.candidateName.notBlank",
                    ValidationMessages.CANDIDATE_NAME_NOT_BLANK);
        }

        // Validate scheduleTime
        if (interviewDto.getScheduleTime() == null) {
            errors.rejectValue("scheduleTime", "error.scheduleTime.notNull",
                    ValidationMessages.SCHEDULE_TIME_NOT_NULL);
        } else if (interviewDto.getScheduleTime().isBefore(LocalDate.now())) {
            errors.rejectValue("scheduleTime", "error.scheduleTime.invalid",
                    ValidationMessages.SCHEDULE_TIME_VALID);
        }

        // Validate scheduleFrom
        if (interviewDto.getScheduleFrom() == null) {
            errors.rejectValue("scheduleFrom", "error.scheduleFrom.notNull",
                    ValidationMessages.SCHEDULE_FROM_NOT_NULL);
        }

        // Validate scheduleTo
        if (interviewDto.getScheduleTo() == null) {
            errors.rejectValue("scheduleTo", "error.scheduleTo.notNull",
                    ValidationMessages.SCHEDULE_TO_NOT_NULL);
        } else if (interviewDto.getScheduleFrom() != null && interviewDto.getScheduleTo().isBefore(interviewDto.getScheduleFrom())) {
            errors.rejectValue("scheduleTo", "error.scheduleTo.invalid",
                    ValidationMessages.SCHEDULE_TIMES_VALID);
        }

        // Validate location
        if (interviewDto.getLocation() == null || interviewDto.getLocation().isEmpty()) {
            errors.rejectValue("location", "error.location.notBlank",
                    ValidationMessages.LOCATION_NOT_BLANK);
        }

        // Validate job
        if (interviewDto.getJob() == null || interviewDto.getJob().isEmpty()) {
            errors.rejectValue("job", "error.job.notBlank",
                    ValidationMessages.JOB_NOT_BLANK);
        }

        // Validate interview
        if (interviewDto.getInterview() == null || interviewDto.getInterview().isEmpty()) {
            errors.rejectValue("interview", "error.interview.notBlank",
                    ValidationMessages.INTERVIEW_NOT_BLANK);
        }

        // Validate recruiterOwner
        if (interviewDto.getRecruiterOwner() == null || interviewDto.getRecruiterOwner().isEmpty()) {
            errors.rejectValue("recruiterOwner", "error.recruiterOwner.notBlank",
                    ValidationMessages.RECRUITER_OWNER_NOT_BLANK);
        }

        // Validate meetingID
        if (interviewDto.getMeetingID() == null || interviewDto.getMeetingID().isEmpty()) {
            errors.rejectValue("meetingID", "error.meetingID.notBlank",
                    ValidationMessages.MEETING_ID_NOT_BLANK);
        }

    }
}
