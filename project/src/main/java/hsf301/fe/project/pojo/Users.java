package hsf301.fe.project.pojo;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Users {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "phone_number", nullable = true)
    private String phone;

    @Column(name = "role")
    private String role;

    @Column(name = "register_date", nullable = true)
    private Date registerDate;

    @Column(name="active")
    private boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Notification> notifications;

}
