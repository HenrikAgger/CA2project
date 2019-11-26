package facadesTest;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import facadesSP.PersonFacade;
import entities.Person;
import entities.Phone;
import errorhandling.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;


public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
  

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */         
    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
       
    }

    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        List<Phone> phones1 = new ArrayList();
       
        CityInfo city1 = new CityInfo("2740", "Skovlunde");
        Address address1 = new Address("Blokhaven 16", "Apartment", city1);
        Person p1 = new Person("j@v.dk", "John", "Travolta", phones1, address1);
        Phone phone1 = new Phone("26739401", "Mobile"); 
       
        
        phones1.add(phone1);
        
        
        
       
        
        
        Hobby boxing = new Hobby("Boxing", "A martial art that focuses on boxing");
        boxing.addPerson(p1);
      
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.persist(p1);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        
    }

     
    @Test
    public void testCount() {
        assertEquals(1, facade.getPersonCount(), "Expects one row in the database");
    }

    @Test
    public void addPersonTest() {
        PersonDTO pdto = new PersonDTO();
        pdto.setFirstName("Ida");
        pdto.setLastName("Larsen");
        pdto.setEmail("ida@cphbusiness.dk");
        pdto.setCity("Helsingør");
        pdto.setStreet("Rosenkildevej");
        pdto.setStreetInfo("Hjemme");
        pdto.setZip("3000");
        List<String> hobbies = new ArrayList();
        List<String> phones = new ArrayList();
        hobbies.add("name:100m Løb,description:Det skal gå hurtigt");
        phones.add("number:55555555,description:Hjemmenummer");
        pdto.setHobbies(hobbies);
        pdto.setPhones(phones);

        facade.addPerson(pdto);
    }
    
    
    @Test
    public void getAllPersonsTest() {
        PersonsDTO persons = facade.getAllPersons();
        assertTrue(persons.getAll().size() == 1);
    }
}
//
//    @Test
//    public void getPersonTest() {
//        try {
//            PersonDTO p = facade.getPersonByID(p1.getId());
//            assertTrue(p.getFirstName().equals("Hans"));
//        } catch (PersonNotFoundException ex) {
//            ex.printStackTrace();
//        }
//    }


//    @Test
//    public void deletePersonTest() {
//        try {
//            PersonDTO p = facade.deletePerson(p1.getId());
//            assertThat(p, Matchers.hasProperty("id"));
//        } catch (PersonNotFoundException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//}
