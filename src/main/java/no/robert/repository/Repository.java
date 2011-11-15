package no.robert.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class Repository {

    private EntityManager entityManager;
    
    private <T> CriteriaQuery<T> createQuery( Class<T> entityType, CriteriaPopulator constraints ) {
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = criteriaBuilder.createQuery(entityType);
        constraints.populate(criteria, criteriaBuilder);
        return criteria;	
    }

    public <T> List<T> find(Class<T> entityType, CriteriaPopulator constraints) {        
    	CriteriaQuery<T> criteria = createQuery( entityType, constraints );
        return entityManager.createQuery(criteria).getResultList();
    }
    
    public <T> T findSingle( Class<T> entityType, CriteriaPopulator constraints) {
    	CriteriaQuery<T> criteria = createQuery( entityType, constraints );
        return entityManager.createQuery(criteria).getSingleResult();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
