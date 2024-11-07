package hsf301.fe.project.service.defines;

import hsf301.fe.project.pojo.Wallet;

public interface IWalletService {
    Wallet getWalletByUserId(Integer userId);
    void addFunds(Integer userId, Long amount);

}
