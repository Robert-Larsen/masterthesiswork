package no.robert.lambda;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Author
{
    @Id
    @GeneratedValue( generator = "increment" )
    @GenericGenerator( name = "increment", strategy = "increment" )
    private Long id;

    private String name;

    public Author()
    {

    }

    public Author( String name )
    {
        this.name = name;
    }

    @Id
    public void setId( Long id )
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}
