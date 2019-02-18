/* jslint node: true */
"use strict";
var crypto = require ('crypto');
var express = require('express');
var router = express.Router();
var model = require("./model.js");
var myNewModel = require("./databasemodel.js");

var genSaltString = function(length){
  return crypto.randomBytes(Math.ceil(length/2))
              .toString('hex')
              .slice(0, length);
};

var sha512 = function(password, saltName){
  var hash = crypto.createHmac('sha512', saltName);
  hash.update(password);
  var value = hash.digest('hex');
  return {
      salt: saltName, 
      passwordHash: value
  };
};
/*
var getSalt = function(userName){
  var obj = null;
  connection.query('SELECT salt FROM logins WHERE userName='+userName, function (err, result){
    if (err){
      throw err;
    } else {
      obj = result;
    }
  })
  return obj;
  */
/*
function saltHashPassword(userpassword) {
  var salt = genSaltString(16); 
  var passwordData = sha512(userpassword, salt);
  console.log('UserPassword = '+userpassword);
    console.log('Passwordhash = '+passwordData.passwordHash);
    console.log('nSalt = '+passwordData.salt);

}*/

router.get('/get', function (req, res) {
  console.log("/get is working");
  res.send("get works");
});

router.post('/post', function (req, res) {
  console.log("/post works");
  res.send("post works");
});

//returns false(if no such user exists) otherwise returns your usertype (typeOfEmployment, bad variable name)
router.post('/login', function(req,res){
  console.log('password');
  console.log('usertype ' + typeof req.body.userName + ' passtype ' 
    + typeof req.body.password);

  //var salt = getSalt(req.body.userName);
  //var salt = null;
  
//  var hashedPass = sha512((req.body.password).toString(), salt);
  //var password = hashedPass.passwordHash;
  //console.log('password ' + password);
  /*myNewModel.getSalt(req.body.userName).then(function(data){
  console.log("---false--- data " + data );
  var salt = data.salt; 
  //console.log("---false--- " + salt.toString()); 
  console.log("---false--- " + salt);*/
  /*getSalt(user).then(function(data){
  var salt = data.salt;

}*/
  /*var data = myNewModel.login.findOne({
    //attributes: ['salt'],
    where: {userName: req.body.userName}
  }).then(function(data){
    console.log("i database getsalt: "+data);
    return data
  });
  console.log("i database getsalt ute: "+data);
console.log("i database getsalt ute: "+data.salt);
*/
  console.log("username: " + req.body.userName);
  myNewModel.getSalt(req.body.userName).then(function(data){
    console.log("Returning salt: " + data.salt);


  var hashedPass = sha512(req.body.password, data.salt);
  console.log("Returning salt hasehd: " + hashedPass.salt);
  console.log("Returning salt hasehd: " + hashedPass.passwordHash);


  myNewModel.loginAttempt(req.body.userName, 
    hashedPass.passwordHash).then(function(data){
    console.log(JSON.stringify(data));
    if(data === null){
      res.send(false);
      console.log("---false---");
    }else{
      console.log("data här "+data.typeOfEmployment);
      if(data.typeOfEmployment == "\"company\""){
        var json = {companyName: data.companyName, userType: data.typeOfEmployment};
        console.log("kommer in hit iaf 11");
        res.json(json);
      }else{
        console.log("alltid här? ");
        res.json({userType: data.typeOfEmployment});
      }
      
      console.log("type of user: " + data.typeOfEmployment);
      console.log("---true---");
    }
  });

  });



  
  
});

//Registers a user, requires: password (hashed), username, userType(cleaner, company), companyName (NULL if cleaner)
router.post('/register', function(req,res){
  console.log("User: " + req.body.userName+ " password: " + req.body.password + " type:"
  + req.body.userType + " company: " + req.body.companyName);
  myNewModel.findExistingUser(req.body.userName).then(function(data){
    if(data === null){
      res.send(false);
      console.log("---Doesn't exist---")
      console.log("---returns---")
      console.log("---false---");
    }else{
      res.send(true);
      console.log("---Does exist---")
      console.log("---returns---")
      console.log("---true---");
    }
  });
  var salt = genSaltString(16); 
  var passwordData = sha512(req.body.password, salt);
  //console.log('UserPassword = '+userpassword);
  console.log('Password = ' + req.body.password);
  console.log('Passwordhash = '+passwordData.passwordHash);
  console.log('nSalt = '+passwordData.salt);

  myNewModel.registerUser(req.body.userName, passwordData.passwordHash, salt,
   JSON.stringify(req.body.userType), req.body.companyName);

});

//Adds a new deal, requires: price, city, hours(estimated time) and a description
router.post('/addDeal', function(req,res){
  var price = req.body.price;
  var city = req.body.city;
  var time = req.body.hours;
  var description = req.body.description;
  var companyName = req.body.companyName;
  myNewModel.createDeal(price, city, time, description, companyName);
  console.log("Deal added");
  res.send("Deal added");
});

//This will return all of your accepted deals, requires: username
router.post('/mytasks', function(req,res){
  var username = req.body.userName;
  console.log("mytasks: " + username);
  myNewModel.getMyAssignments(username).then(function(data){
    res.json(data);
  });
});

//Get all deals that are not accepted, requires: Nothing...
router.get('/getDeals', function(req,res){
  console.log("Getting Deals!");
  myNewModel.getDeals().then(function(data){
    res.json(data);
  });

});

router.post('/companyGetDeals', function(req,res){
  console.log("Company get their deals!");
  myNewModel.companyGetDeals(req.body.companyName).then(function(data){
    console.log("Sending data to for the company: " + req.body.companyName + " (agreements)");
    res.json(data);
  });
});

router.post('/companyGetOngoingAssignments', function(req,res){
  myNewModel.companyGetOngoingAssignments(req.body.companyName).then(function(data){
    console.log("Sending data to for the company: " + req.body.companyName + " (ongonigassignments)");
    res.json(data);
  });
});
module.exports = router;
