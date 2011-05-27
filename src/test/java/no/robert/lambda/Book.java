package no.robert.lambda;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Books")
public class Book
{
    private String title;   
    
    @ManyToMany
    private Set<Author> authors;

    @OneToOne
    private Publisher publisher;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    
    private int pages;
    private double price;
    private boolean available;
    private boolean isPaperback;

    public Book( String title, Author author, int pages, Publisher publisher )
    {
        this( title, author, pages, publisher, -1.00, false );
    }

    public Book( String title, Author author, int pages )
    {
        this(title, author, pages, null, -1.00, false );
    }

    public Book()
    {
    }

    public Book(String title, Author author, int pages, Publisher publisher, double price, boolean paperback )
    {
        this.title = title;
        this.authors = new HashSet<Author>();
        this.authors.add( author );
        this.pages = pages;
        this.publisher = publisher;
        this.price = price;
        this.isPaperback = paperback;        
        this.available = true;       
    }
    
    public Book( String title, Author author, int pages, double price )
    {
        this( title, author, pages, null, price, false );      
    }

    public Long getId()
    {
        return id;
    }

    @Id
    public void setId( Long id )
    {
        this.id = id;
    }

    public int getPages()
    {
        return this.pages;
    }

    public void setPages( int pages )
    {
        this.pages = pages;
    }

    public double getPrice()
    {
        return this.price;
    }
    
    public void setPrice( double price )
    {
        this.price = price;
    }
    
    public Set<Author> getAuthors()
    {
        return this.authors;
    }

    public void setAuthors( Set<Author> authors )
    {
        this.authors = authors;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public boolean isAvailable()
    {
        return this.available;
    }

    public void setAvailable( boolean available )
    {
        this.available = available;
    }

    public boolean hasPaperback()
    {
        return this.isPaperback;
    }

    public void setPaperback( boolean paperback)
    {
        this.isPaperback = paperback;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Publisher getPublisher() {
        return publisher;
    }
 }
