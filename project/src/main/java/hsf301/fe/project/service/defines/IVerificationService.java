package hsf301.fe.project.service.defines;

public interface IVerificationService {
    public void saveCode(String email, String code);
    public boolean verifyCode(String email, String code);
}
