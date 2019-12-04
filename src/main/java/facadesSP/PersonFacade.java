package facadesSP;

import DTO.PersonDTO;
import DTO.PersonDTOMapper;
import DTO.PersonsDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import errorhandling.PersonNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private static PersonDTOMapper mapper = new PersonDTOMapper();

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //TODO Remove/Change this before use
    //@Override
    public long getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }
    }

    //@Override
    public PersonDTO addPerson(PersonDTO personDTO) {
        EntityManager em = getEntityManager();
        Person person = mapper.DTOMapper(personDTO);
        try {
            em.getTransaction().begin();

            List<Hobby> toAdd = new ArrayList();
            List<Hobby> toRemove = new ArrayList();

            person.getHobbies().removeAll(toRemove);
            person.getHobbies().addAll(toAdd);
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);

    }

    //@Override
    public PersonDTO getPersonByID(long id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        if (id <= 0) {
            throw new PersonNotFoundException("Person not found on the id " + id);
        }
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException("Person not found on the id " + id);
        }
        return new PersonDTO(person);
    }

    //@Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        List<Person> persons = em.createQuery("SELECT p FROM Person p").getResultList();
        PersonsDTO personsDTO = new PersonsDTO(persons);
        return personsDTO;
    }

    public PersonsDTO getAllPersonsByHobby(String name) {
        EntityManager em = getEntityManager();
        Hobby hobby = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :name", Hobby.class).setParameter("name", name).getSingleResult();
        PersonsDTO persons = new PersonsDTO(hobby.getPersons());
        return persons;
    }

    public PersonsDTO getAllPersonsByCity(String zipCode) {
        EntityManager em = getEntityManager();
        List<Address> addresses = em.createQuery("SELECT a FROM Address a WHERE a.cityInfo.zipCode = :zipCode").setParameter("zipCode", zipCode).getResultList();

        List<Person> persons = new ArrayList<>();
        for (Address address : addresses) {
            persons.addAll(address.getPerson());
        }
        PersonsDTO personsDTO = new PersonsDTO(persons);
        return personsDTO;
    }

    //@Override
    public PersonDTO getPersonInfoByPhone(String number) throws PersonNotFoundException {
        EntityManager em = getEntityManager();

        try {
            Person person = em.createQuery("SELECT p.person FROM Phone p WHERE p.number = :number", Person.class)
                    .setParameter("number", number).getSingleResult();
            if (person == null) {
                throw new PersonNotFoundException("Person not found on the  " + number);
            }
            PersonDTO personDTO = new PersonDTO(person);
            return personDTO;
        } finally {
            em.close();;
        }
    }

    public int getNoOfPersonsGivenHobby(String hobbyName) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            Hobby hobby = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :name", Hobby.class)
                    .setParameter("name", hobbyName).getSingleResult();
            if (hobby == null) {
                throw new PersonNotFoundException("Person not found on the  " + hobbyName);
            }
            return hobby.getPersons().size();
        } finally {
            em.close();
        }
    }

    public String getZipCodesDK() throws MalformedURLException, IOException {
        URL url = new URL("http://dawa.aws.dk/postnumre/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("User-Agent", "server"); //remember if you are using SWAPI
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null){
            response.append(line);
        }
        rd.close();
        return response.toString();
    }

    public void createPerson(PersonDTO personDTO){
        EntityManager em = getEntityManager();
        try {
            //Person person = new Person(personDTO.getEmail(), personDTO.getFirstName(), personDTO.getLastName(), personDTO.getPhones(), new Address(personDTO.getStreet(), personDTO.getStreetInfo(), new CityInfo(personDTO.getZip(), personDTO.getCity())));   //.String firstName, String lastName, List<Phone> phones,Address address);
            Person person = new PersonDTOMapper().DTOMapper(personDTO);
            
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            
        } catch (Exception e) {
        }
            
        
    }
    
    
    
    
    
    
    //@Override
    public void populatePersons() {

        EntityManager em = emf.createEntityManager();

        // CityInfo 
        CityInfo city3 = new CityInfo("2860", "Søborg");
        CityInfo city4 = new CityInfo("2630", "Taastrup");
        CityInfo city5 = new CityInfo("2400", "København NV");
        CityInfo city6 = new CityInfo("2200", "København N");
        CityInfo city7 = new CityInfo("3650", "Ølstykke");
        CityInfo city8 = new CityInfo("2610", "Rødovre");
        CityInfo city9 = new CityInfo("2820", "Gentofte");
        CityInfo city10 = new CityInfo("2100", "København Ø");

        // Addresses
        Address address5 = new Address("Dalen 19", "House", city3);
        Address address6 = new Address("Højvangen 38", "House", city3);
        Address address7 = new Address("Høje Gladsaxe 55", "Apartment", city3);
        Address address8 = new Address("Søborg Parkalle 28", "Townhouse", city3);
        Address address9 = new Address("Parkvej 32", "Apartment", city4);
        Address address10 = new Address("Gadehavegårdsvej 34", "Apartment", city4);
        Address address11 = new Address("Rugkærgårdsvej 16", "House", city4);
        Address address12 = new Address("Bisiddervej 19", "Apartment", city5);
        Address address13 = new Address("Bogtrykkervej 22", "Apartment", city5);
        Address address14 = new Address("Titangade 67", "Apartment", city6);
        Address address15 = new Address("Blågardsgade 3", "Apartment", city6);
        Address address16 = new Address("Lokes Plads 21", "Townhouse", city7);
        Address address17 = new Address("Tårnvej 151", "Townhouse", city8);
        Address address18 = new Address("Ørnegårdsvej 17", "Apartment", city9);
        Address address19 = new Address("Hobrogade 16", "Apartment", city10);
        Address address20 = new Address("Randersgade 22", "Apartment", city10);

        // Lists of Phones
        List<Phone> phones1 = new ArrayList();
        List<Phone> phones2 = new ArrayList();
        List<Phone> phones3 = new ArrayList();
        List<Phone> phones4 = new ArrayList();
        List<Phone> phones5 = new ArrayList();
        List<Phone> phones6 = new ArrayList();
        List<Phone> phones7 = new ArrayList();
        List<Phone> phones8 = new ArrayList();
        List<Phone> phones9 = new ArrayList();
        List<Phone> phones10 = new ArrayList();
        List<Phone> phones11 = new ArrayList();
        List<Phone> phones12 = new ArrayList();
        List<Phone> phones13 = new ArrayList();
        List<Phone> phones14 = new ArrayList();
        List<Phone> phones15 = new ArrayList();
        List<Phone> phones16 = new ArrayList();
        List<Phone> phones17 = new ArrayList();
        List<Phone> phones18 = new ArrayList();
        List<Phone> phones19 = new ArrayList();
        List<Phone> phones20 = new ArrayList();
        List<Phone> phones21 = new ArrayList();
        List<Phone> phones22 = new ArrayList();
        List<Phone> phones23 = new ArrayList();
        List<Phone> phones24 = new ArrayList();
        List<Phone> phones25 = new ArrayList();

        // Phonenumbers
        Phone phone1 = new Phone("26739401", "Mobile");
        phones1.add(phone1);

        Phone phone2 = new Phone("23423432", "Mobile");
        phones2.add(phone2);
        Phone phone3 = new Phone("12345678", "Work");
        phones3.add(phone3);
        Phone phone4 = new Phone("21040201", "Mobile");
        phones4.add(phone4);
        Phone phone5 = new Phone("56789034", "Mobile");
        phones5.add(phone5);
        Phone phone6 = new Phone("24567834", "Work");
        phones6.add(phone6);
        Phone phone7 = new Phone("28764954", "Mobile");
        phones7.add(phone7);
        Phone phone8 = new Phone("75867402", "Work");
        phones7.add(phone8);
        Phone phone9 = new Phone("24356785", "Mobile");
        phones8.add(phone9);
        Phone phone10 = new Phone("24569134", "Mobile");
        phones9.add(phone10);
        Phone phone11 = new Phone("12374842", "Home");
        phones10.add(phone11);
        Phone phone12 = new Phone("84747467", "Mobile");
        phones11.add(phone12);
        Phone phone13 = new Phone("48467874", "Mobile");
        phones12.add(phone13);
        Phone phone14 = new Phone("52245234", "Home");
        phones13.add(phone14);
        Phone phone15 = new Phone("28345635", "Mobile");
        phones14.add(phone15);
        Phone phone16 = new Phone("34763422", "Mobile");
        phones15.add(phone16);
        Phone phone17 = new Phone("23845234", "Home");
        phones16.add(phone17);
        Phone phone18 = new Phone("91235424", "Mobile");
        phones17.add(phone18);
        Phone phone19 = new Phone("80280248", "Work");
        phones18.add(phone19);
        Phone phone20 = new Phone("23563465", "Mobile");
        phones19.add(phone20);
        Phone phone21 = new Phone("23563422", "Home");
        phones20.add(phone21);
        Phone phone22 = new Phone("87969430", "Mobile");
        phones21.add(phone22);
        Phone phone23 = new Phone("34563634", "Mobile");
        phones22.add(phone23);
        Phone phone24 = new Phone("80604933", "Work");
        phones23.add(phone24);
        Phone phone25 = new Phone("91235424", "Mobile");
        phones24.add(phone25);
        Phone phone26 = new Phone("90124954", "Mobile");
        phones25.add(phone26);

        // Persons
        Person p1 = new Person("j@v.dk", "John", "Travolta", phones1, address14);
        Person p5 = new Person("l@u.dk", "Laurids", "Ulf", phones5, address8);
        Person p6 = new Person("a@b.dk", "Ali", "Bashir", phones6, address15);
        Person p7 = new Person("p@e.dk", "Payam", "Eldin", phones7, address20);
        Person p8 = new Person("e@f.dk", "Erik", "Frandsen", phones8, address13);
        Person p10 = new Person("n@s.dk", "Nick", "Sanders", phones10, address12);
        Person p11 = new Person("l@s.dk", "Luke", "Skywalker", phones11, address12);
        Person p12 = new Person("u@h.dk", "Umar", "Habib", phones12, address10);
        Person p13 = new Person("c@j.dk", "Claus", "Jensen", phones13, address5);
        Person p14 = new Person("b@l.dk", "Berta", "Larsen", phones14, address11);
        Person p15 = new Person("h@k.dk", "Henriette", "Kaj", phones15, address17);
        Person p16 = new Person("c@i.dk", "Connie", "Ipsen", phones16, address6);
        Person p17 = new Person("j@d.dk", "Jack", "Danson", phones17, address16);
        Person p18 = new Person("p@h.dk", "Patrik", "Harry", phones18, address7);
        Person p19 = new Person("s@j.dk", "Shpat", "Jakupi", phones19, address7);
        Person p21 = new Person("z@h.dk", "Zeki", "Hadi", phones21, address9);
        Person p22 = new Person("n@h.dk", "Niels", "Hansen", phones22, address9);
        Person p23 = new Person("m@d.dk", "Mads", "Davidsen", phones23, address18);
        Person p24 = new Person("y@t.dk", "Yusuf", "Tahir", phones24, address19);

        phone1.setPerson(p1);
        phone5.setPerson(p5);
        phone6.setPerson(p6);
        phone7.setPerson(p7);
        phone8.setPerson(p8);
        phone10.setPerson(p10);
        phone11.setPerson(p11);
        phone12.setPerson(p12);
        phone13.setPerson(p13);
        phone14.setPerson(p14);
        phone15.setPerson(p15);
        phone16.setPerson(p16);
        phone17.setPerson(p17);
        phone18.setPerson(p18);
        phone19.setPerson(p19);
        phone21.setPerson(p21);
        phone22.setPerson(p22);
        phone23.setPerson(p23);
        phone24.setPerson(p24);

        // Different hobbies
        Hobby boxing = new Hobby("Boxing", "A martial art that focuses on boxing");
        boxing.addPerson(p1);
        boxing.addPerson(p8);
        boxing.addPerson(p13);
        boxing.addPerson(p15);
        boxing.addPerson(p18);
        boxing.addPerson(p22);
        boxing.addPerson(p23);

        Hobby powerlifting = new Hobby("Powerlifting", "A sport about competing in lifting the most weight in squat, benchpress, and deadlift.");
        powerlifting.addPerson(p1);
        powerlifting.addPerson(p5);
        powerlifting.addPerson(p6);
        powerlifting.addPerson(p7);
        powerlifting.addPerson(p8);
        powerlifting.addPerson(p12);

        powerlifting.addPerson(p18);
        powerlifting.addPerson(p19);

        powerlifting.addPerson(p22);

        Hobby swimming = new Hobby("Swimming", "Swimmers can swim for health benefits or as a competition sport.");

        swimming.addPerson(p10);
        swimming.addPerson(p11);
        swimming.addPerson(p12);
        swimming.addPerson(p5);
        swimming.addPerson(p7);

        Hobby archery = new Hobby("Archery", "Archers shoot arrows at a specific target.");
        archery.addPerson(p13);
        archery.addPerson(p14);
        archery.addPerson(p15);
        archery.addPerson(p5);
        archery.addPerson(p6);
        archery.addPerson(p11);

        archery.addPerson(p21);

        Hobby taekwondo = new Hobby("Taekwondo", "A martial art that focuses on various kicking techniques.");
        taekwondo.addPerson(p1);

        taekwondo.addPerson(p5);
        taekwondo.addPerson(p10);
        taekwondo.addPerson(p14);
        taekwondo.addPerson(p15);
        taekwondo.addPerson(p18);
        taekwondo.addPerson(p19);

        taekwondo.addPerson(p22);
        taekwondo.addPerson(p24);

        try {
            em.getTransaction().begin();
            em.persist(boxing);
            em.persist(powerlifting);
            em.persist(swimming);
            em.persist(archery);
            em.persist(taekwondo);
            em.getTransaction().commit();

        } finally {
            em.close();
        }

    }

}
