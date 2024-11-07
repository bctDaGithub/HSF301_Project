package hsf301.fe.project.service.implement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

import hsf301.fe.project.service.defines.IVerificationService;

@Service
public class VerificationService implements IVerificationService {

    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> expiryStorage = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 1000; // 5 phút

    public void saveCode(String email, String code) {
        codeStorage.put(email, code);
        expiryStorage.put(email, System.currentTimeMillis() + EXPIRATION_TIME_MS);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = codeStorage.get(email);
        Long expiryTime = expiryStorage.get(email);

        if (storedCode != null && storedCode.equals(code) && expiryTime != null && expiryTime > System.currentTimeMillis()) {
            codeStorage.remove(email);
            expiryStorage.remove(email);
            return true;
        }
        return false;
    }
}
