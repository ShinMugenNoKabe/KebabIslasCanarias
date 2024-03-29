package es.rufino.kebab.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ShinMugenNoKabe
 */
@Entity
@Data
@Builder
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private final String name;

    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
    private List<Role> roles;
    
}
