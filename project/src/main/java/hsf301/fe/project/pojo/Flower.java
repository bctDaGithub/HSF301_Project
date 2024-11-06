package hsf301.fe.project.pojo;

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

@Entity
@Table(name = "flowers")
@Getter
@Setter
@NoArgsConstructor
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flowerId;
    
    private String type; 
    private int quantity; 
    private String condition; 
    private double price; 

    @ManyToOne
    @JoinColumn(name = "seller_id") 
    private Users seller;
}
