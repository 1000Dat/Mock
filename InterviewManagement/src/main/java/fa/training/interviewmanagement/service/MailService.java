package fa.training.interviewmanagement.service;

public interface MailService {
    void sendEmail(String to, String subject, String body);
}
