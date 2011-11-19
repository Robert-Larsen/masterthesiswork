package no.robert.repository.dsl;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static no.robert.methodref.MethodRef.on;
import static no.robert.repository.dsl.Where.having;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import no.robert.lambda.Author;
import no.robert.lambda.Book;
import no.robert.lambda.Editor;
import no.robert.lambda.Publisher;
import no.robert.repository.Repository;

import org.hibernate.mapping.AuxiliaryDatabaseObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import sun.security.krb5.internal.rcache.AuthTime;

public class DslCriteriaTest {

	private Repository repository;
	private EntityManager entityManager;
	private Book howToBeAwesome, anotherBook, howToBeAwesome2;
	private Author author, anotherAuthor;
	private Publisher publisher, anotherPublisher;
	private Editor editor, anotherEditor;

	@Before
	public void setupPersistenceContext() {
		entityManager = createEntityManagerFactory("no.robert.lambda").createEntityManager();
		repository = new Repository();
		repository.setEntityManager(entityManager);

		entityManager.getTransaction().begin();

		author = new Author("Rune Flobakk");
		anotherAuthor = new Author("Robert Larsen");
		editor = new Editor("Some editor");
		anotherEditor = new Editor( "Some other editor");
		publisher = new Publisher("Manning");
		anotherPublisher = new Publisher("Addison-Wesley");

		howToBeAwesome = new Book("How To Be Awesome", author, 1337, publisher);
		howToBeAwesome2 = new Book( "How To Be Awesome 2.0", author, 1338, anotherPublisher);
		anotherBook = new Book("Title", anotherAuthor, 10, anotherPublisher);
		entityManager.persist(howToBeAwesome);
		entityManager.persist(howToBeAwesome2);	
		entityManager.persist(anotherBook);
	
	}

	@Test
	public void specifySingleProperty() {
		List<Book> books = repository.find(Book.class, having(on(Book.class).getTitle()).equal("How To Be Awesome"));
		assertThat(books, hasSize(1));
		assertThat(books, hasItem(howToBeAwesome));
	}

	@Test
	public void specifySinglePropertyInChain() {
		List<Book> books = repository.find( Book.class, having( on( Book.class ).getPublisher().getName()).equal( "Addison-Wesley" ) );
		assertThat(books, hasSize(2));
		assertThat(books, hasItem(anotherBook));
		assertThat(books, hasItem(howToBeAwesome2));
	}

	@Test
	public void specifyCollectionProperty() {
		List<Book> books = repository.find(Book.class, having(on(Book.class).getAuthors()).with( author ) );
		assertThat( books, hasSize(2)); 
		assertThat( books, hasItem(howToBeAwesome));
		assertThat( books, hasItem(howToBeAwesome2));
	}

	@Test
	public void specifyCollectionPropertyInChain() {
		List<Book> books = repository.find( Book.class, 
				having(on(Book.class).getAuthors()).having(on(Author.class).getName()).equal( "Rune Flobakk" ));
		
		
		assertThat(books, hasSize(2));
		assertThat(books, hasItem(howToBeAwesome));
		assertThat(books, hasItem(howToBeAwesome2));
	}

	@After
	public void rollback() {
		entityManager.getTransaction().rollback();
	}

}
