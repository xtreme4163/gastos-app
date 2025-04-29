package io.gastos.gastos_app.service.gasto;

import io.gastos.gastos_app.model.Gasto;
import io.gastos.gastos_app.model.Gasto_;
import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.model.user.UserEntry_;
import io.gastos.gastos_app.service.GastosCrudFacade;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GastoFacade {
    public static final Logger log = LoggerFactory.getLogger(GastoFacade.class);

    private GastosCrudFacade gastosCrudFacade;

    public GastoFacade(GastosCrudFacade crud) {
        this.gastosCrudFacade = crud;
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public Gasto create(Gasto gasto){
        try{
           Gasto attached =  gastosCrudFacade.create(gasto);
           log.info("Gasto guardado con id={}" + attached.getId());
           return attached;
        }catch (Exception ex){
            log.info("Error guardando gasto" + ex.getMessage());
        }
        return null;
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public void deleteGasto(Gasto gasto){
        gastosCrudFacade.delete(gasto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public List<Gasto> findByUser(UserEntry user) {
        CriteriaBuilder cb = gastosCrudFacade.getCriteriaBuilder();
        CriteriaQuery<Gasto> cq = cb.createQuery(Gasto.class);
        Root<Gasto> root = cq.from(Gasto.class);
        Predicate p1 = cb.equal(root.get(Gasto_.usuario).get(UserEntry_.id), user.getId());
        cq.select(root)
                .where(p1);
        TypedQuery<Gasto> query = gastosCrudFacade.createQuery(cq);

        try {
            return query.getResultList();
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }

    }

    @PreAuthorize("hasRole('OWNER')")
    public List<Gasto> findAll(){
        return gastosCrudFacade.findAll(Gasto.class);
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public Gasto editGasto(Gasto gasto){
        return gastosCrudFacade.update(gasto);
    }

    @PreAuthorize("hasRole('OWNER')")
    public Gasto findById(Long id){
        return gastosCrudFacade.find(Gasto.class, id)
                .orElse(null);
    }

}
