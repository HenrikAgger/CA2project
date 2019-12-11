package restSP;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.PersonNotFoundException;
import utils.EMF_Creator;
import facadesSP.PersonFacade;
import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/CA2",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    public PersonResource(){}
    
    @GET
    @Path("populate")
    @Produces({MediaType.APPLICATION_JSON})
    public String populate(){
        FACADE.populatePersons();
        return "{\"msg\":\"done!\"}";
    }
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws PersonNotFoundException {
        PersonDTO pdto = FACADE.getPersonByID(id);
        return Response.ok(GSON.toJson(pdto)).build();
    
     }
    @GET
    @Path("/phone/{phone}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPhone(@PathParam("phone") String phone) throws PersonNotFoundException {
        PersonDTO pdto = FACADE.getPersonInfoByPhone(phone);
        return Response.ok(GSON.toJson(pdto)).build();
    }
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll(){
        PersonsDTO persons = FACADE.getAllPersons();
        return Response.ok(GSON.toJson(persons.getAll())).build();
    }
    
    @GET
    @Path("all/hobby/{hobbyName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllByHobby(@PathParam("hobbyName") String name){
        PersonsDTO persons = FACADE.getAllPersonsByHobby(name);
        return Response.ok(GSON.toJson(persons.getAll())).build();
    }
    
    @GET
    @Path("all/city/{zip}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllByCity(@PathParam("zip") String zip){
        PersonsDTO persons = FACADE.getAllPersonsByCity(zip);
        return Response.ok(GSON.toJson(persons.getAll())).build();
        
    }
    
    @GET
    @Path("hobby/count/{hobbyName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getNoOfPersonsGivenHobby(@PathParam("hobbyName") String hobbyName) throws PersonNotFoundException{
        int count = FACADE.getNoOfPersonsGivenHobby(hobbyName);
        return Response.ok("{\"personCount\":" + count + "}").build();
    }
    
    @GET
    @Path("zip/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getZipCodesDK() throws IOException{
        String zips = FACADE.getZipCodesDK();
        return Response.ok(GSON.toJson(zips)).build();
    }
    
    @POST
    @Path("addperson")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addPerson(String data) throws IOException {
        PersonDTO addPerson = GSON.fromJson(data, PersonDTO.class);
        FACADE.createPerson(addPerson);
        return Response.ok("{\"message\":done}").build();
        
    }
    
    
    }