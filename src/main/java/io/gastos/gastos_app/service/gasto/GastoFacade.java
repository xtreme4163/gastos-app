package io.gastos.gastos_app.service.gasto;

import io.gastos.gastos_app.model.Gasto;
import io.gastos.gastos_app.service.GastosCrudFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

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

    public List<Gasto> findAll(){
        return gastosCrudFacade.findAll(Gasto.class);
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    public Gasto editGasto(Gasto gasto){
        return gastosCrudFacade.update(gasto);
    }

    public Gasto findById(Long id){
        return gastosCrudFacade.find(Gasto.class, id)
                .orElse(null);
    }

}
