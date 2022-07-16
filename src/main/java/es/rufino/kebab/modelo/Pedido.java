/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class) // Para que funciones los CreatedDate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codpedido;

    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Boolean recogidaEnTienda;

    private String direccionDeEnvio;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private List<LineaPedido> listaDeLineasDePedido;

    public void anadeLineaDePedido(LineaPedido lp) {
        this.getListaDeLineasDePedido().add(lp);
    }

    public Pedido(Usuario usuario, Date fecha, Boolean recogidaEnTienda, List<LineaPedido> listaDeLineasDePedido) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.recogidaEnTienda = recogidaEnTienda;
        this.listaDeLineasDePedido = listaDeLineasDePedido;
    }

}
