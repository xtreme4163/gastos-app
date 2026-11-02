package io.gastos.gastos_app.service.user;

import io.gastos.gastos_app.model.Gasto;
import io.gastos.gastos_app.model.user.TipoUsuario;
import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.model.user.UserEntry_;
import io.gastos.gastos_app.model.util.consultauser.FiltroConsultaUsuario;
import io.gastos.gastos_app.service.GastosCrudFacade;
import io.gastos.gastos_app.service.gasto.GastoFacade;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional()
@Repository
public class UserFacade {

    private final GastosCrudFacade gastosCrudFacade;
    private final GastoFacade gastoFacade;

    public UserFacade(GastosCrudFacade f, GastoFacade gastoFacade){
        gastosCrudFacade=f;
        this.gastoFacade = gastoFacade;
    }

    public UserEntry findByPassAndUsername(String pass, String username){
        CriteriaBuilder cb = gastosCrudFacade.getCriteriaBuilder();
        CriteriaQuery<UserEntry> cq = cb.createQuery(UserEntry.class);
        Root<UserEntry> root = cq.from(UserEntry.class);
        Predicate p1 = cb.equal(root.get(UserEntry_.username), username);
        Predicate p2 = cb.equal(root.get(UserEntry_.pass), pass);

        cq.select(root)
                .where(cb.and(p1, p2));

        TypedQuery<UserEntry> query = gastosCrudFacade.createQuery(cq);

        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Optional<UserEntry> findByUsername(String username){
        CriteriaBuilder cb = gastosCrudFacade.getCriteriaBuilder();
        CriteriaQuery<UserEntry> cq = cb.createQuery(UserEntry.class);
        Root<UserEntry> root = cq.from(UserEntry.class);
        Predicate p1 = cb.equal(root.get(UserEntry_.username), username);
        cq.select(root)
                .where(p1);

        TypedQuery<UserEntry> query = gastosCrudFacade.createQuery(cq);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        CriteriaBuilder cb = gastosCrudFacade.getCriteriaBuilder();
        CriteriaQuery<UserEntry> cq = cb.createQuery(UserEntry.class);
        Root<UserEntry> root = cq.from(UserEntry.class);
        Predicate p1 = cb.equal(root.get(UserEntry_.username), username);
        cq.select(root)
                .where(p1);
        TypedQuery<UserEntry> query = gastosCrudFacade.createQuery(cq);

        try {
            Optional<UserEntry> user = Optional.ofNullable(query.getSingleResult());
            return user.isPresent();
        } catch (NoResultException ex) {
            return false;
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public UserEntry create(UserEntry usuario, String passHash){
        usuario.setPass(passHash);
        return gastosCrudFacade.create(usuario);
    }

    /**
     * Busca usuarios filtrando por nombre (contiene, sin distinguir mayúsculas) y tipo.
     * Los filtros nulos o vacíos se ignoran.
     */
    public List<UserEntry> findByFiltro(FiltroConsultaUsuario filtro) {
        CriteriaBuilder cb = gastosCrudFacade.getCriteriaBuilder();
        CriteriaQuery<UserEntry> cq = cb.createQuery(UserEntry.class);
        Root<UserEntry> root = cq.from(UserEntry.class);
        List<Predicate> predicados = new ArrayList<>();

        boolean hayFiltroNombre = filtro.getUsername() != null && !filtro.getUsername().isBlank();
        boolean hayFiltroTipo = filtro.getTipoUsuario() != null;

        if (hayFiltroNombre) {
            String patron = "%" + filtro.getUsername().trim().toLowerCase() + "%";
            predicados.add(cb.like(cb.lower(root.get(UserEntry_.username)), patron));
        }
        if (hayFiltroTipo) {
            predicados.add(cb.equal(root.get(UserEntry_.tipoUsuario), filtro.getTipoUsuario()));
        }

        cq.select(root)
                .where(predicados.toArray(new Predicate[0]))
                .orderBy(cb.asc(root.get(UserEntry_.username)));

        return gastosCrudFacade.createQuery(cq).getResultList();
    }

    /**
     * Elimina el usuario junto con todos sus gastos en una misma transacción.
     * @return false si el usuario no existe
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public boolean deleteConGastos(Long idUsuario) {
        Optional<UserEntry> usuario = gastosCrudFacade.find(UserEntry.class, idUsuario);
        if (usuario.isEmpty()) {
            return false;
        }
        for (Gasto gasto : gastoFacade.findByUser(usuario.get())) {
            gastoFacade.deleteGasto(gasto);
        }
        gastosCrudFacade.delete(usuario.get());
        return true;
    }

}
