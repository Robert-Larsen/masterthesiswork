package no.robert.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


public class Repository {

    private EntityManager entityManager;

    public <T> List<T> find(Class<T> entityType, CriteriaPopulator constraints) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = criteriaBuilder.createQuery(entityType);
        constraints.populate(criteria, criteriaBuilder);
        return entityManager.createQuery(criteria).getResultList();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
