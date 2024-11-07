package hsf301.fe.project.service.defines;

public interface IEmailService {
    public boolean sendVerificationCode(String toEmail, String code);
}
