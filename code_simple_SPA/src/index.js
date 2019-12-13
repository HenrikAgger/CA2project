import "bootstrap/dist/css/bootstrap.css";

var url="http://henriksdomainname.dk/CA2";

function getHobbyAndCities(){
  fetch(url+"/api/person/hobby/all")
    .then(data => data.json())
    .then(data => {
      console.log(data);
      data.forEach(hobby => {
        var hobbies = document.getElementById("hobbies");
        var option = document.createElement("option");
        option.text = hobby.name;
        option.value = hobby.name;
        hobbies.add(option);
      });
    });
    fetch(url+"/api/person/city/all")
    .then(data => data.json())
    .then(data => {
      console.log(data);
      data.forEach(city => {
        var cities = document.getElementById("cities");
        var option = document.createElement("option");
        option.text = city.zipCode;
        option.value = city.zipCode;
        cities.add(option);
      });
    });
}
getHobbyAndCities();


function getAllPersonsByHobby(hobby) {
  document.getElementById("PersonInHobby").innerHTML="";
  fetch(url+"/api/person/all/hobby/"+hobby)
    .then(data => data.json())
    .then(data => {
      console.log(data);
      data.forEach(person => {
        var li = document.createElement("li");
        li.appendChild(document.createTextNode(person.firstName));
        document.getElementById("PersonInHobby").appendChild(li);
      });
    });
}

getAllPersonsByHobby("swimming");

function getAllPersonsInCity(zipcode) {
  document.getElementById("PersonInzip").innerHTML="";
  fetch(url + "/api/person/all/city/"+zipcode)
    .then(data => data.json())
    .then(data => {
      console.log(data);
      data.forEach(person => {
        var li = document.createElement("li");
        li.appendChild(document.createTextNode(person.firstName));
        document.getElementById("PersonInzip").appendChild(li);
      });
    });
}
getAllPersonsInCity(2860);


function getNoOfPersonsByHobby(hobby) {
  fetch(url + "/api/person/hobby/count/"+hobby)
    .then(data => data.json())
    .then(data => {
      console.log(data);
      document.getElementById("phcount").innerHTML= data.personCount;
    });
}
getNoOfPersonsByHobby("swimming");

function getNoOfAllZipDK() {
  fetch("http://dawa.aws.dk/postnumre")
    .then(data => data.json())
    .then(data => {
      data.forEach(city => {
        var zips = document.getElementById("zips");
        var option = document.createElement("option");
        option.text = city.nr;
        zips.add(option);
      });
    });
}
getNoOfAllZipDK();

window.onload = function() {
  document.getElementById("createPerson").onsubmit = submit;
  
  document.getElementById("cities").onchange = cityChange;
  document.getElementById("hobbies").onchange = hobbyChange;
};

function cityChange(){
  var zipCode = document.getElementById("cities").value;
  getAllPersonsInCity(zipCode);

}

function hobbyChange(){
  var hobby = document.getElementById("hobbies").value;
  getAllPersonsByHobby(hobby);
  getNoOfPersonsByHobby(hobby);
}

function submit(e) {
  e.preventDefault();

  var email = document.getElementById("email").value;
  var firstName = document.getElementById("firstName").value;
  var lastName = document.getElementById("lastName").value;
  var street = document.getElementById("street").value;
  var streetInfo = document.getElementById("streetInfo").value;
  var zip = document.getElementById("zip").value;
  var city = document.getElementById("city").value;

  var person = {
    id: 0,
    email: email,
    firstName: firstName,
    lastName: lastName,
    street: street,
    streetInfo: streetInfo,
    zip: zip,
    city: city,
    hobbies: [],
    phones: []
  };
  
  var _url = url + "/api/person/addperson";
  fetch(_url, {
    method: "POST",
    headers: {
        'Content-Type': 'application/json'
      },
    body: JSON.stringify(person)
  });
}
