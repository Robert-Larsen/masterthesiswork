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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DslCriteriaTest {

	private Repository repository;
	private EntityManager entityManager;
	private Book howToBeAwesome;
	private Author author, anotherAuthor;
	private Book anotherBook;
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
		anotherBook = new Book("Title", anotherAuthor, 10, anotherPublisher);
		entityManager.persist(howToBeAwesome);
		entityManager.persist(anotherBook);
	}

	@Test
	public void specifySingleProperty() {
		List<Book> books = repository.find(Book.class, having(on(Book.class).getTitle()).eq("How To Be Awesome"));
		assertThat(books, hasSize(1));
		assertThat(books, hasItem(howToBeAwesome));
	}

	@Test
	public void specifySinglePropertyInChain() {
		List<Book> books = repository.find( Book.class, having( on( Book.class ).getPublisher().getName()).eq( "Addison-Wesley" ) );
		assertThat(books, hasSize(1));
		assertThat(books, hasItem(anotherBook));
	}

	@Test
	public void specifyCollectionProperty() {
		List<Book> books = repository.find(Book.class, having(on(Book.class).getAuthors()).with( anotherAuthor ) );
		assertThat( books, hasSize(1));
		assertThat( books, hasItem(anotherBook));
	}

	@Test
	public void specifyCollectionPropertyInChain() {
		List<Book> books = repository.find( Book.class, 
				having(on(Book.class).getAuthors()).having(on(Author.class).getName()).eq( "Rune Flobakk" ));
		assertThat( books, hasSize(1));
		assertThat( books, hasItem(howToBeAwesome));
	}

	@After
	public void rollback() {
		entityManager.getTransaction().rollback();
	}

}
