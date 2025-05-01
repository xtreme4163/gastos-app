package io.gastos.gastos_app.model;

import io.gastos.gastos_app.model.user.UserEntry;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "gasto")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "concepto", nullable = false)
    @NotBlank(message = "El concepto del gasto es obligatorio")
    @NotNull(message = "El concepto es obligatorio")
    private String concepto;

    @Column(name="importe")
    @NotNull(message = "El importe es obligatorio")
    private BigDecimal importe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_gasto_usuario"))
    private UserEntry usuario;


    @Column(name = "fecha")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaGasto;

    public Gasto() {}

    public Long getId() {
        return id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getImporte() {
        return importe;
    }
    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public LocalDate getFechaGasto() {
        return fechaGasto;
    }

    public void setFechaGasto(LocalDate fechaGasto) {
        this.fechaGasto = fechaGasto;
    }

    public UserEntry getUsuario() {
        return usuario;
    }

    public void setUsuario(UserEntry usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Gasto{" +
                "id=" + id +
                ", tipoGasto='" + concepto + '\'' +
                ", importe=" + importe +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Gasto gasto = (Gasto) o;
        return Objects.equals(id, gasto.id) && Objects.equals(concepto, gasto.concepto) && Objects.equals(importe, gasto.importe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, concepto, importe);
    }
}
