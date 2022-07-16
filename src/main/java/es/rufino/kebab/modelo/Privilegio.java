/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.modelo;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "privilegios")
public class Privilegio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    @ManyToMany(mappedBy = "listaDePrivilegios", fetch = FetchType.EAGER)
    private List<Rol> listaDeRoles;

    public Privilegio(String nombre) {
        this.nombre = nombre;
    }
    
}
