/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facadesSP;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import errorhandling.PersonNotFoundException;
import java.util.List;

/**
 *
 * @author Younes
 */
public interface IPersonFacade {
    
    
  public long getPersonCount();
  public PersonDTO getPersonByID(long id) throws PersonNotFoundException;
  public PersonsDTO getAllPersons();
  public PersonsDTO getPersonsByHobby(String name);
  public PersonsDTO getPersonsByCity(String city);
  public long getPersonCountByHobby(String name);

  public PersonDTO getPersonInfoByPhone(int number); 
  public PersonDTO addPerson(PersonDTO p);  
  public PersonDTO editPerson(Person p) throws PersonNotFoundException;  
  public PersonDTO deletePerson(long id) throws PersonNotFoundException;
  public void populatePersons();  
}
