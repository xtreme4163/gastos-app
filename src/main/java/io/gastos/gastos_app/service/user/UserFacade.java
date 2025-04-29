package io.gastos.gastos_app.service.user;

import io.gastos.gastos_app.model.user.UserEntry;
import io.gastos.gastos_app.model.user.UserEntry_;
import io.gastos.gastos_app.service.GastosCrudFacade;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional()
@Repository
public class UserFacade {

    private final GastosCrudFacade gastosCrudFacade;

    public UserFacade(GastosCrudFacade f){
        gastosCrudFacade=f;
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

}
