import "bootstrap/dist/css/bootstrap.css";

function getAllPersonsByHobby() {
  fetch("https://henriksdomainname.dk/CA2/api/person/all/hobby/swimming")
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

getAllPersonsByHobby();

function getAllPersonsInCity() {
  fetch("https://henriksdomainname.dk/CA2/api/person/all/city/2630")
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

getAllPersonsInCity();

function getNoOfPersonsByHobby() {
  fetch("https://henriksdomainname.dk/CA2/api/person/hobby/count/swimming")
    .then(data => data.json())
    .then(data => {
      console.log(data);
      document.getElementById("phcount").append(" " + data.personCount);
    });
}
getNoOfPersonsByHobby();

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
};

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
  
  var url = "https://henriksdomainname.dk/CA2/api/person/addperson";
  fetch(url, {
    method: "POST",
    headers: {
        'Content-Type': 'application/json'
      },
    body: JSON.stringify(person)
  });
}
