package no.robert.lambda;

import static no.robert.lambda.LambdaCriteria.having;
import static no.robert.lambda.LambdaCriteria.on;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import no.robert.lambda.Author;
import no.robert.lambda.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class LambdaCriteriaTest
{
    private LambdaRepository repository;

    private Publisher manning, addisonWesley;
    private Author authorRobert, authorRune;
    private Editor editorManning, editorWesley;
    EntityManagerFactory entityMgrFactory;
    EntityManager entityManager;

    @Before
    public void setUp()
    {
        entityMgrFactory = Persistence.createEntityManagerFactory( "no.robert.lambda" );
        entityManager = entityMgrFactory.createEntityManager();
        authorRobert = new Author( "Robert" );
        authorRune = new Author( "Rune" );
        editorManning = new Editor( "Rune" );
        editorWesley = new Editor( "Robert" );
        manning = new Publisher( "Manning", editorManning );
        addisonWesley = new Publisher( "Addison-Wesley", editorWesley );
        entityManager.persist( manning );
        entityManager.persist( addisonWesley );
        entityManager.persist( authorRobert );
        entityManager.persist( authorRune );
        entityManager.persist( editorManning );
        entityManager.persist( editorWesley );

        repository = new LambdaRepository();
        repository.setEntityManager( entityManager );
        entityManager.getTransaction().begin();
    }

    @After
    public void tearDown()
    {
        entityManager.getTransaction().rollback();
    }

    @Test
    @Ignore
    public void eq()
    {
        entityManager.persist( new Book( "A book", authorRobert, 12, addisonWesley ) );
        entityManager.persist( new Book( "Another book", authorRune, 12, manning ) );
        entityManager.persist( new Book( "A third book", authorRune, 12, manning ) );

        List<Book> books = repository.find( having( Book.class, on( Book.class ).getTitle() ).eq( "Another book" ) );
        assertThat( books.size(), is( 1 ) );

        Book b = repository.findSingle( having( Book.class, on( Book.class).getTitle() ).eq( "A book" ) );
        assertThat( b.getTitle(), is( "A book" ) );

        List<Book> wesleyBooks =
        	repository.find( having( Book.class, on( Book.class ).getPublisher().getName() ).eq( "Addison-Wesley" ) );
        assertThat( wesleyBooks.size(), is( 1 ) );

        List<Book> manningBooks = repository.find( having( Book.class, on( Book.class ).getPublisher().getEditor().getName() ).eq( "Rune" ) );
        assertThat( manningBooks.size(), is( 2 ) );
    }

    @Test
    public void with()
    {
    	entityManager.persist( new Book("Someting", authorRune, 100) );
    	entityManager.persist( new Book("Someting else", authorRune, 100) );
    	entityManager.persist( new Book("Someting different", authorRune, 100) );

    	List<Book> booksByAuthor =
    		  repository.find( having( Book.class, on( Book.class).getAuthors() ).with( on( Author.class ).getName() ).eq( null ) );

    	for( Object o : booksByAuthor )
    		System.out.println(o.toString());
    }

    @Test
    @Ignore
    public void greaterThan()
    {
        entityManager.persist( new Book( "A book with more than 10 pages", authorRune, 11, manning ) );

        Book b = repository.findSingle( having( Book.class, on( Book.class ).getPages() ).greaterThan( 10 ) );
        assertThat( b.getTitle(), is( "A book with more than 10 pages" ) );

        entityManager.persist( new Book( "An expensive book", authorRune, 50, 90.0 ) );
        entityManager.persist( new Book( "Another expensive book", authorRune, 40, 100.0 ) );

        List<Book> expensiveBooks = repository.find( having( Book.class, on( Book.class ).getPrice() ).greaterThan( 90.0 ) );

        assertThat( expensiveBooks.size(), is( 1 ) );

    }

    @Test
    @Ignore
    public void greaterThanOrEqualTo()
    {
        entityManager.persist( new Book( "A book with more than 100 pages", authorRune, 102, 100.5 ) );
        Book b = repository.findSingle( having( Book.class, on( Book.class ).getPages() ).greaterThanOrEqualTo( 102 ) );
        assertThat( b.getTitle(), is( "A book with more than 100 pages" ) );

        entityManager.persist( new Book( "An expensive book", authorRune, 50, 500.01 ) );
        entityManager.persist( new Book( "Another expensive book", authorRune, 40, 1299.99 ) );

        List<Book> expensiveBooks = repository.find( having( Book.class, on( Book.class ).getPrice() ).greaterThanOrEqualTo( 500.01 ) );

        assertThat( expensiveBooks.size(), is( 2 ) );
    }

    @Test
    @Ignore
    public void lessThan()
    {
        entityManager.persist( new Book( "A book with less than 100 pages", authorRune, 90, 100.5 ) );
        Book b = repository.findSingle( having( Book.class, on( Book.class ).getPages() ).lessThan( 100 ) );
        assertThat( b.getTitle(), is( "A book with less than 100 pages" ) );

        entityManager.persist( new Book( "A cheap book", authorRune, 50, 29.90 ) );
        entityManager.persist( new Book( "Another cheap book", authorRune, 40, 9.99 ) );

        List<Book> expensiveBooks = repository.find( having( Book.class, on( Book.class ).getPrice() ).lessThan(  50.00 ) );

        assertThat( expensiveBooks.size(), is( 2 ) );
    }

    @Test
    @Ignore
    public void lessThanOrEqualTo()
    {
        entityManager.persist( new Book( "A book with less than 100 pages", authorRune, 90, 100.5 ) );
        Book b = repository.findSingle( having( Book.class, on( Book.class ).getPages() ).lessThanOrEqualTo( 90 ) );
        assertThat( b.getTitle(), is( "A book with less than 100 pages" ) );

        entityManager.persist( new Book( "A cheap book", authorRune, 50, 29.90 ) );
        entityManager.persist( new Book( "Another cheap book", authorRune, 40, 9.99 ) );

        List<Book> expensiveBooks = repository.find( having( Book.class, on( Book.class ).getPrice() ).lessThanOrEqualTo(  29.90 ) );

        assertThat( expensiveBooks.size(), is( 2 ) );
    }

    @Test
    @Ignore
    public void getAll()
    {
        entityManager.persist( new Book( "A book", authorRune, 12, manning ) );
        entityManager.persist( new Book( "Another book", authorRune, 12, manning ) );

        List<Book> allBooks = repository.find( having( Book.class, on( Book.class ) ).getAll() );
        assertThat( allBooks.size(), is( 2 ) );
    }
}
