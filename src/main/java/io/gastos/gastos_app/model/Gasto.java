package io.gastos.gastos_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "gasto")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="tipo_gasto")
    @NotBlank(message = "El tipo de gasto es obligatorio")
    private String tipoGasto;

    @Column(name="importe")
    @NotNull(message = "El importe es obligatorio")
    private BigDecimal importe;

    public Gasto() {}

    public Long getId() {
        return id;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }
    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public BigDecimal getImporte() {
        return importe;
    }
    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    @Override
    public String toString() {
        return "Gasto{" +
                "id=" + id +
                ", tipoGasto='" + tipoGasto + '\'' +
                ", importe=" + importe +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Gasto gasto = (Gasto) o;
        return Objects.equals(id, gasto.id) && Objects.equals(tipoGasto, gasto.tipoGasto) && Objects.equals(importe, gasto.importe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoGasto, importe);
    }
}
