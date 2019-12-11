import "bootstrap/dist/css/bootstrap.css";

function getAllPersonsByHobby() {
  fetch("https://henriksdomainname.dk/CA2/api/person/all/hobby/boxing")
    .then(data => data.json())
    .then(data => {
      console.log(data);
      data.forEach(person => {
        document.getElementById("index").append(" " + person.firstName);
        // console.log(person.firstName);
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
        document.getElementById("index").append(" " + person.firstName);
      });
    });
}

getAllPersonsInCity();

function getNoOfPersonsByHobby() {
  fetch("https://henriksdomainname.dk/CA2/api/person/hobby/count/boxing")
    .then(data => data.json())
    .then(data => {
      console.log(data);
      document.getElementById("index").append(" " + data.personCount);
    });
}
getNoOfPersonsByHobby();

function getNoOfAllZipDK() {
  fetch("http://dawa.aws.dk/postnumre")
    .then(data => data.json())
    .then(data => {
      data.forEach(city => {
        document.getElementById("zips").append(" " + city.nr);
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
