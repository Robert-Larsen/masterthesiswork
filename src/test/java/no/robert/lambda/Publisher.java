package no.robert.lambda;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Publisher {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    private String name;
    
    @OneToOne
    private Editor editor;

    public Publisher()
    {
        
    }

    public Publisher(String name)
    {
        this.setName(name);
    }

    public Publisher( String name, Editor editor )
    {
        this.name = name;
        this.editor = editor;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    
    public void setEditor( Editor editor )
    {
        this.editor = editor;
    }
    
    public Editor getEditor()
    {
        return editor;
    }

}
