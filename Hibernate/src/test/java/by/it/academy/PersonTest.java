package by.it.academy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonTest {
    SessionFactory factory;
    StandardServiceRegistry registry;
    @Before
    public void setUp() {
        registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.test.cfg.xml")
                .build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    @Test
    public void createAndReadAndDelete(){
        //create
        Person person = new Person();
        person.setAge(25);
        person.setName("alex");
        person.setSecondName("pupkin");
        String personId = save(person);
        assertNotNull(personId);
        //read
        Person readedPerson = get(personId);
        assertEquals(person, readedPerson);
        //delete
        delete(personId);
        Person deletedPerson = get(personId);
        assertNull(deletedPerson);
    }

    @After
    public void tearDown() {
        StandardServiceRegistryBuilder.destroy(registry);
        factory.close();
    }
    private Person get(String personId) {
        Transaction transaction = null;
        Person person = null;
        try (Session session = factory.openSession();){
            transaction = session.beginTransaction();
            person = session.get(Person.class, personId);
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return person;
    }

    private String save(Person person) {
        String id = null;
        Transaction transaction = null;
        try (Session session = factory.openSession();){
            transaction = session.beginTransaction();
            id = (String) session.save(person);
            transaction.commit();

        } catch (Exception e) {
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return id;
    }
    private void delete(String personId){
        String id = null;
        Transaction transaction = null;
        try (Session session = factory.openSession();){
            transaction = session.beginTransaction();
            Person person = session.get(Person.class, personId);
            session.delete(person);
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}