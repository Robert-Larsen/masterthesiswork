package no.robert.lambda;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public class LambdaRepository
{    
    private EntityManager entityManager;

    public void setEntityManager( EntityManager entityManager )
    {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager()
    {
        return entityManager;
    }
    
    public <T> List<T> find( CriteriaQuery<T> query )
    {
        return entityManager.createQuery( query ).getResultList();
    }
    
    public <T> T findSingle( CriteriaQuery<T> query )
    {
        return entityManager.createQuery( query ).getSingleResult();
    }

}
