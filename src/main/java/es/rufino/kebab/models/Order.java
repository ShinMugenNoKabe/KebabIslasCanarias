package es.rufino.kebab.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class) // We need in order to make "orderedAt" work
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderedAt;

    private Boolean pickupAtStore;

    private String address; // TODO: Make Address table

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderLine> orderLines;

}
