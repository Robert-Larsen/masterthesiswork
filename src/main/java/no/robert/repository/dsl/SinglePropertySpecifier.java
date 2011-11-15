package no.robert.repository.dsl;


import static java.lang.reflect.Modifier.isFinal;
import static no.robert.repository.dsl.Strategies.asProperty;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.hamcrest.core.IsNull;

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
		Set<Root<?>> roots = criteria.getRoots();
		Root root;
		if( roots.size() > 0 ) {
			root = roots.iterator().next();
			Subquery<?> subquery = criteria.subquery(methodRef.getTargetType());
			Root subroot = subquery.from( methodRef.getTargetType() );
			subquery.select(subroot);
			Path<Object> property = subroot.get( asProperty( methodRef ).getName() );
			subquery.where( builder.equal( property, propertyValue ) );
			criteria.where(builder.in(root).value(subquery));
		}
		else {
			root = criteria.from(methodRef.getTargetType());
			Path<Object> path = root.get(asProperty(methodRef).getName());
			criteria.select(root);

			MethodRef next = methodRef.nextInChain();
			if( next.getReturnType() != null && !next.getReturnType().equals(Void.TYPE)) { 
				while(next.getReturnType() != null && !next.getReturnType().equals(Void.TYPE)) {

					Subquery<?> subquery = criteria.subquery(next.getTargetType());
					Root subroot = subquery.from( next.getTargetType() );
					subquery.select(subroot);
					Path<Object> property = subroot.get( asProperty( next ).getName() );
					subquery.where( builder.equal( property, propertyValue ) );

					criteria.where(builder.in(path).value(subquery));
					next = next.nextInChain();
				}
			}
			else {
				Path<Object> property = root.get(asProperty(methodRef).getName());
				criteria.where(builder.equal(property, propertyValue));
			}
		}
	}
}



