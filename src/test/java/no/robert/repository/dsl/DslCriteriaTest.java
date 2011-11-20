package no.robert.repository.dsl;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static no.robert.methodref.MethodRef.on;
import static no.robert.repository.dsl.Where.having;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import no.robert.lambda.Author;
import no.robert.lambda.Book;
import no.robert.lambda.Publisher;
import no.robert.repository.Repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DslCriteriaTest {

	private Repository repository;
	private EntityManager entityManager;
	private Book howToBeAwesome, anotherBook, howToBeAwesome2;
	private Author author, anotherAuthor;
	private Publisher publisher, anotherPublisher;

	@Before
	public void setupPersistenceContext() {
		entityManager = createEntityManagerFactory("no.robert.lambda").createEntityManager();
		repository = new Repository();
		repository.setEntityManager(entityManager);

		entityManager.getTransaction().begin();

		author = new Author("Rune Flobakk");
		anotherAuthor = new Author("Robert Larsen");
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

		books = repository.find(Book.class, having(on(Book.class).getPages()).greaterThan(1338));
		assertTrue(books.isEmpty());
		
		books = repository.find(Book.class, having(on(Book.class).getPages()).greaterThan(1000));
		assertThat(books, hasSize(2));
		assertThat(books, hasItems(howToBeAwesome, howToBeAwesome2));
		
		books = repository.find(Book.class, having(on(Book.class).getPages()).greaterThanOrEqualTo(10));
		assertThat(books, hasSize(3));
		assertThat(books, hasItems(howToBeAwesome, howToBeAwesome2, anotherBook));
		
		books = repository.find(Book.class, having(on(Book.class).getPages()).lessThan(11));
		assertThat(books, hasSize(1));
		assertThat(books, hasItem(anotherBook));
		
		books = repository.find(Book.class, having(on(Book.class).getPages()).lessThanOrEqualTo(1337));
		assertThat(books, hasSize(2));
		assertThat(books, hasItems(howToBeAwesome, anotherBook));
	}

	@Test
	public void specifySinglePropertyInChain() {
		List<Book> books = repository.find( Book.class, having( on( Book.class ).getPublisher().getName()).equal( "Addison-Wesley" ) );
		assertThat(books, hasSize(2));
		assertThat(books, hasItems(anotherBook, howToBeAwesome2));
	}

	@Test
	public void specifyCollectionProperty() {
		List<Book> books = repository.find(Book.class, having(on(Book.class).getAuthors()).with( author ) );
		assertThat( books, hasSize(2)); 
		assertThat( books, hasItems(howToBeAwesome, howToBeAwesome2));
	}

	@Test
	public void specifyCollectionPropertyInChain() {
		List<Book> books = repository.find( Book.class, 
				having(on(Book.class).getAuthors()).having(on(Author.class).getName()).equal( "Rune Flobakk" ));

		assertThat(books, hasSize(2));
		assertThat(books, hasItems(howToBeAwesome, howToBeAwesome2));
	}

	@After
	public void rollback() {
		entityManager.getTransaction().rollback();
	}

}
