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
import no.robert.repository.Repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DslCriteriaTest {

    private Repository repository;
    private EntityManager entityManager;
    private Book howToBeAwesome;
    private Author author;
    private Book anotherBook;

    @Before
    public void setupPersistenceContext() {
        entityManager = createEntityManagerFactory("no.robert.lambda").createEntityManager();
        repository = new Repository();
        repository.setEntityManager(entityManager);

        entityManager.getTransaction().begin();

        author = new Author("Rune Flobakk");
        howToBeAwesome = new Book("How To Be Awesome", author, 1337);
        anotherBook = new Book("Title", author, 10);
        entityManager.persist(howToBeAwesome);
        entityManager.persist(anotherBook);
    }

    @Test
    public void specifySingleProperty() {
        List<Book> books = repository.find(Book.class, having(on(Book.class).getTitle()).eq("How To Be Awesome"));
        assertThat(books, hasSize(1));
        assertThat(books, hasItem(howToBeAwesome));

        repository.find(Book.class, having(on(Book.class).getAuthors()).with(author));
        // repository.find(Book.class,
        // having(on(Book.class).getAuthors()).having(on(Author.class).getName()));
    }

    @After
    public void rollback() {
        entityManager.getTransaction().rollback();
    }

}
