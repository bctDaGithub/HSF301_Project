package hsf301.fe.project.pojo;

import jakarta.persistence.*;


@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WalletID", nullable = false)
    private Integer id;

    @Column(name = "UserID", nullable = false)
    private Integer userID;

    @Column(name = "Amount", precision = 10, scale = 2)
    private Long amount;

    public Wallet(Integer userID, Long amount) {
        this.userID = userID;
        this.amount = amount;
    }

    public Wallet() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}