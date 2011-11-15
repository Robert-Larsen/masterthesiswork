package no.robert.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import no.robert.methodref.MethodRef;

public interface CriteriaPopulator {

    public static CriteriaPopulator NO_OP = new CriteriaPopulator() {
        @Override
        public <T> void populate(CriteriaQuery<T> criteria, CriteriaBuilder builder) {
        }
    };

    <T> void populate(CriteriaQuery<T> criteria, CriteriaBuilder builder);
}
