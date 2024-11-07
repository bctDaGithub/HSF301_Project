package hsf301.fe.project.pojo;

<<<<<<< Updated upstream
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
=======
import jakarta.persistence.*;
import lombok.*;
>>>>>>> Stashed changes

@Entity
@Table(name = "flowers")
@Getter
@Setter
@NoArgsConstructor
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< Updated upstream
    private int flowerId;
    
    private String type; 
    private int quantity; 
    private String condition; 
    private double price; 
=======
    private int id;

    @Column(nullable = false)
    private String name;

    private String type; 
    private int quantity; 
    private String condition;

    private double price;
    @Column(name = "image_url")
    private String imageUrl;
>>>>>>> Stashed changes

    @ManyToOne
    @JoinColumn(name = "seller_id") 
    private Users seller;
}
