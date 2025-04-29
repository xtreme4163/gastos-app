package io.gastos.gastos_app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GastosCrudFacade {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public <T> T create(T entity) {
        try {
            em.persist(entity);
            return entity;
        } catch (ConstraintViolationException ex) {
            throw ex;
        }
    }

    @Transactional
    public <T> T update(T entity) {
        try {
            return em.merge(entity);
        } catch (ConstraintViolationException ex) {
            throw ex;
        }
    }

    @Transactional
    public <T> void delete(T entity) {
        em.remove(entity);
    }


    public <T, ID> Optional<T> find(Class<T> clazz, ID id) {
        return Optional.ofNullable(em.find(clazz, id));
    }

    public <T> List<T> findAll(Class<T> clazz) {
        String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e";
        return em.createQuery(jpql, clazz).getResultList();
    }
    
    public CriteriaBuilder getCriteriaBuilder(){
        return em.getCriteriaBuilder();
    }

    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> cq) {
        return em.createQuery(cq);
    }
}
