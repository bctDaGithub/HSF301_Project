package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.Transaction;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.pojo.Wallet;
import hsf301.fe.project.repository.TransactionRepository;
import hsf301.fe.project.repository.WalletRepository;
import hsf301.fe.project.service.defines.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class WalletService implements IWalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    public Wallet getWalletByUserId(Integer userId) {
        return walletRepository.findbyuserid(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user ID: " + userId));
    }
    public void addFunds(Integer userId, Long amount) {
        Wallet wallet = getWalletByUserId(userId);
        wallet.setAmount(wallet.getAmount() + amount);
        walletRepository.save(wallet);
        Transaction transaction = new Transaction();
        transaction.setWalletID(wallet);
        transaction.setAmount(amount);
        ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime now = ZonedDateTime.now(vietnamZone);
        transaction.setTime(now.toInstant());
        transaction.setTransactionType("Top-up");
        transaction.setStatus("Completed");
        transactionRepository.save(transaction);
    }

    @Override
    public void save(Users users) {
        walletRepository.save(new Wallet(users.getId(), 0L));
    }
}
