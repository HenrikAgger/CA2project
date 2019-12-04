/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Younes
 */
public class PersonDTO {
    
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String street;
    private String streetInfo;
    private String zip;
    private String city;
    private List<String> hobbies = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
 
  
     
    public PersonDTO() {
    }   
    
    public PersonDTO(Person person) {
        this.id = person.getId();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.street = person.getAddress().getStreet();
        this.streetInfo = person.getAddress().getStreet();
        this.zip = person.getAddress().getCityInfo().getZipCode();
        this.city = person.getAddress().getCityInfo().getCity();
        
        for(Hobby hobby : person.getHobbies()){
            hobbies.add("name:" + hobby.getName() + ", description:" + hobby.getDescription());
        }
        
        for(Phone phone : person.getPhones()){
            phones.add("number:" + phone.getNumber() + ",description:" + phone.getDescription());
        }
       
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetInfo() {
        return streetInfo;
    }

    public void setStreetInfo(String streetInfo) {
        this.streetInfo = streetInfo;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }


  
    
    
}
