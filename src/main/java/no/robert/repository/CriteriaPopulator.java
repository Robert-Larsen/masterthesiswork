package no.robert.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;

import no.robert.methodref.MethodRef;

public interface CriteriaPopulator {
	
    public static CriteriaPopulator NO_OP = new CriteriaPopulator() {
        @Override
        public <T> void populate(CriteriaQuery<T> criteria, CriteriaBuilder builder) {
        }
        
        @Override
        public Path<?> getPreviousPath() {
        	return null;
        }
    };

    <T> void populate(CriteriaQuery<T> criteria, CriteriaBuilder builder);
    
    Path<?> getPreviousPath();
}
