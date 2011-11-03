package no.robert.repository.dsl;


import static no.robert.repository.dsl.Strategies.asProperty;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import no.robert.methodref.MethodRef;
import no.robert.repository.CriteriaPopulator;

public class SinglePropertySpecifier<PROP> implements CriteriaPopulator {


    private final MethodRef methodRef;
    private final CriteriaPopulator previous;
    private PROP propertyValue;

    public SinglePropertySpecifier(MethodRef methodRef, CriteriaPopulator previous) {
        this.methodRef = methodRef;
        this.previous = previous;
    }

    public SinglePropertySpecifier<PROP> eq(PROP value) {
        this.propertyValue = value;
        return this;
    }

    @Override
    public <T> void populate(CriteriaQuery<T> criteria, CriteriaBuilder builder) {
        previous.populate(criteria, builder);
        Root<?> root = criteria.from(methodRef.getTargetType());
        Path<Object> property = root.get(asProperty(methodRef).getName());
        criteria.where(builder.equal(property, propertyValue));
    }
}
