package no.robert.lambda;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Subquery;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import no.robert.methodref.MethodRef;

import static java.lang.reflect.Modifier.isFinal;

public class LambdaCriteria<T, R, S>
{
    private final Method method;

    private final Class<T> type;

    private List<FieldResolver> fieldResolverStrategies = new ArrayList<FieldResolver>();

    private final EntityManagerFactory entityManagerFactory;

    public static <T> T on( final Class<T> type )
    {
    	return MethodRef.on( type );
    }

    public LambdaCriteria( Method method, Class<T> type )
    {
        this.method = method;
        this.type = type;
        entityManagerFactory = Persistence.createEntityManagerFactory( "no.robert.lambda" );
        fieldResolverStrategies.add( new DefaultFieldResolver() );
    }
    
    public LambdaCriteria( Class<T> type )
    {
        this.type = type;
        this.method = null;
        entityManagerFactory = Persistence.createEntityManagerFactory( "no.robert.lambda" );
        fieldResolverStrategies.add( new DefaultFieldResolver() );
    }

    public static <T, R, S> LambdaCriteria<T, R, S> having( Class<T> type, R expression )
    {
    	return new LambdaCriteria<T, R, S>( type );
    }
    
    public LambdaCriteria<T, R, S> with( S expression )
    {
    	return new LambdaCriteria<T, R, S>( type );
    }

    public CriteriaQuery<T> greaterThan( R expression )
    {
        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery( type );
        MethodRef methodRef = MethodRef.get();
        
        if ( expression instanceof java.lang.Number )
        {
            Path<Number> property = criteria.from( type ).get( asProperty( methodRef.getName() ).getName() );
            return criteria.where( builder.gt( property, ( Number ) expression ) );
        }
        return null;
    }

    public CriteriaQuery<T> greaterThanOrEqualTo( R expression )
    {        
        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery( type );
        MethodRef methodRef = MethodRef.get();
        
        if ( expression instanceof java.lang.Number )
        {           
            Path<Number> property = criteria.from( type ).get( asProperty( methodRef.getName() ).getName() );            
            return criteria.where( builder.ge( property, ( Number ) expression ) );
        }
        return null;
    }

    public CriteriaQuery<T> lessThan( R expression )
    {
        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery( type );
        MethodRef methodRef = MethodRef.get();
        
        if ( expression instanceof java.lang.Number )
        {
            Path<Number> property = criteria.from( type ).get( asProperty( methodRef.getName() ).getName() );
            return criteria.where( builder.lt( property, ( Number ) expression ) );
        }
        return null;
    }
    
    public CriteriaQuery<T> lessThanOrEqualTo( R expression )
    {
        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery( type );
        MethodRef methodRef = MethodRef.get();
        
        if ( expression instanceof java.lang.Number )
        {
            Path<Number> property = criteria.from( type ).get( asProperty( methodRef.getName() ).getName() );
            return criteria.where( builder.le( property, ( Number ) expression ) );
        }
        return null;
    }

    public CriteriaQuery<T> getAll()
    {
        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery( type );
        Root<T> cat = criteria.from( type );
        criteria.select( cat );

        return criteria;
    }
    
    public CriteriaQuery<T> eqWhenNoInvocationChain( R expression, CriteriaBuilder criteriaBuilder, MethodRef methodRef )
    {
    	 CriteriaQuery<T> criteria = criteriaBuilder.createQuery( type );
         Path<Object> property = criteria.from( type ).get( asProperty( methodRef.getName() ).getName() );
         return criteria.where( criteriaBuilder.equal( property, expression ) );
    	
    }
    
    @SuppressWarnings("unchecked")
	public CriteriaQuery<T> eq( R expression )
    {
    	CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
    	MethodRef methodRef = MethodRef.get();
    	//methodRef = MethodRef.get();
    	//System.out.println(methodRef.getName());
//    	methodRef = MethodRef.get();
//    	System.out.println(methodRef.getName());
//    	methodRef = MethodRef.get();
//    	System.out.println(methodRef.getName());
    	if( isFinal( methodRef.getReturnType().getModifiers() ) ) {
    		return eqWhenNoInvocationChain( expression, criteriaBuilder, methodRef );
    	}
    	
    	Class previousType = this.type;
    	List<Root<Object>> roots = new ArrayList<Root<Object>>();
    	List<Subquery> subqueries = new ArrayList<Subquery>();
    	List<Path<Object>> paths = new ArrayList<Path<Object>>();
    	int rootcounter = 0;
    	
    	CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
    	Root from = criteriaQuery.from( type );
    	roots.add( from );
    	CriteriaQuery<T> select = criteriaQuery.select(from);
      	
    	do
    	{
    		if( isFinal( methodRef.getReturnType().getModifiers() ) )
    		{
    			Subquery q = subqueries.get( subqueries.size()-1 );
    			subqueries.remove( subqueries.size()-1 );
    			String property = asProperty( methodRef.getName(), previousType ).getName();
    	    	q.where( criteriaBuilder.equal( roots.get( roots.size()-1 ).get( property ), expression) );
    	    	if( subqueries.isEmpty() )
    	    		subqueries.add( q );
    	    	else
    	    		subqueries.add( subqueries.size()-1, q );
    	    	
    	    	for( int i = subqueries.size()-2; i > 0; i-- )
    	    	{
    	    		Subquery qu = subqueries.get( i );
    	    		subqueries.remove( i );
    	    		qu.where( criteriaBuilder.in( paths.get( i ) ).value( i+1 ) );
    	    		subqueries.add( i, qu );
    	    	}    	    	
    	    	select.where(criteriaBuilder.in( paths.get( 0 ) ).value( subqueries.get( 0 ) ) );    	
    	    	return select; 
    		}
    		
    		String fieldname = asProperty( methodRef.getName(), previousType ).getName();
    		Root<Object> root = roots.get( rootcounter );
    		Path<Object> path = root.get( fieldname );
         	
    		Subquery subquery = select.subquery( methodRef.getReturnType() );
        	Root newRoot = subquery.from( methodRef.getReturnType() );
        	subquery.select( newRoot );
        	
        	paths.add( path );
        	subqueries.add( subquery );
        	roots.add( newRoot );
        	previousType = methodRef.getReturnType();
        	rootcounter++;
    	} while( (methodRef = methodRef.nextInChain() ) != null );
    	
    	return select;
    }
    
    
    private Field asProperty( Method method )
    {    	
        for ( FieldResolver fieldResolver : fieldResolverStrategies )
        {
            Field field = fieldResolver.resolveFrom( type, method );
            if ( field != null )
                return field;
        }
        throw new RuntimeException( "Unable to resolve field from " + method.getName() + "() in " + type );
    }
    
    private Field asProperty( String methodname )
    {    	
    	 for ( FieldResolver fieldResolver : fieldResolverStrategies )
         {    		 
             Field field = fieldResolver.resolveFrom( type, methodname );
             if ( field != null )
                 return field;
         }
         throw new RuntimeException( "Unable to resolve field from " + methodname + "() in " + type );
    	
    }

    private Field asProperty( Method method, Class t )
    {
        for ( FieldResolver fieldResolver : fieldResolverStrategies )
        {
            Field field = fieldResolver.resolveFrom( t, method );
            if ( field != null )
                return field;
        }
        throw new RuntimeException( "Unable to resolve field from " + method.getName() + "() in " + t );
    }
    
    private Field asProperty(  String methodname, Class t )
    {
    	for ( FieldResolver fieldResolver : fieldResolverStrategies )
        {
            Field field = fieldResolver.resolveFrom( t, methodname );
            if ( field != null )
                return field;
        }
        throw new RuntimeException( "Unable to resolve field from " + methodname + "() in " + t );
    }
}
