package io.gastos.gastos_app.model;

import io.gastos.gastos_app.model.user.UserEntry;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

/**
 * Tipo de gasto configurable por cada usuario.
 */
@Entity
@Table(name = "tipo_gasto",
        uniqueConstraints = @UniqueConstraint(name = "uk_tipo_gasto_usuario_nombre",
                columnNames = {"id_usuario", "nombre"}))
public class TipoGasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    @NotBlank
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tipo_gasto_usuario"))
    private UserEntry usuario;

    public TipoGasto() {}

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UserEntry getUsuario() {
        return usuario;
    }

    public void setUsuario(UserEntry usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "TipoGasto{id=" + id + ", nombre='" + nombre + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TipoGasto that = (TipoGasto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}