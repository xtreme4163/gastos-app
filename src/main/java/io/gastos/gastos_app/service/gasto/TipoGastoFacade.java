package io.gastos.gastos_app.service.tipogasto;

import io.gastos.gastos_app.model.TipoGasto;
import io.gastos.gastos_app.model.TipoGasto_;
import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.model.user.UserEntry_;
import io.gastos.gastos_app.service.GastosCrudFacade;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoGastoFacade {

    private final GastosCrudFacade gastosCrudFacade;

    public TipoGastoFacade(GastosCrudFacade crud) {
        this.gastosCrudFacade = crud;
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public TipoGasto create(TipoGasto tipoGasto) {
        return gastosCrudFacade.create(tipoGasto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public void delete(TipoGasto tipoGasto) {
        gastosCrudFacade.delete(gastosCrudFacade.update(tipoGasto));
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public TipoGasto findById(Long id) {
        return gastosCrudFacade.find(TipoGasto.class, id).orElse(null);
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public List<TipoGasto> findByUser(UserEntry user) {
        CriteriaBuilder cb = gastosCrudFacade.getCriteriaBuilder();
        CriteriaQuery<TipoGasto> cq = cb.createQuery(TipoGasto.class);
        Root<TipoGasto> root = cq.from(TipoGasto.class);
        Predicate esDelUsuario = cb.equal(root.get(TipoGasto_.usuario).get(UserEntry_.id), user.getId());
        cq.select(root)
                .where(esDelUsuario)
                .orderBy(cb.asc(root.get(TipoGasto_.nombre)));
        return gastosCrudFacade.createQuery(cq).getResultList();
    }

    /** Comprueba si el usuario ya tiene un tipo con ese nombre (sin distinguir mayúsculas). */
    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public boolean existsByUserAndNombre(UserEntry user, String nombre) {
        CriteriaBuilder cb = gastosCrudFacade.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<TipoGasto> root = cq.from(TipoGasto.class);
        Predicate esDelUsuario = cb.equal(root.get(TipoGasto_.usuario).get(UserEntry_.id), user.getId());
        Predicate mismoNombre = cb.equal(cb.lower(root.get(TipoGasto_.nombre)), nombre.trim().toLowerCase());
        cq.select(cb.count(root))
                .where(cb.and(esDelUsuario, mismoNombre));
        return gastosCrudFacade.createQuery(cq).getSingleResult() > 0;
    }
}