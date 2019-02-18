/* jslint node: true */
"use strict";

var express = require('express');
var router = express.Router();
var model = require("./model.js");
var myNewModel = require("./databasemodel.js");

router.get('/get', function (req, res) {
  console.log("/get is working");
  res.send("get works");
});

router.post('/post', function (req, res) {
  console.log("/post works");
  res.send("post works");
});

myNewModel.getSalt(req.body.userName).then(function(data){
  var salt = data.salt; 
  console.log("this is my salt: " + salt);
});

//returns false(if no such user exists) otherwise returns your usertype (typeOfEmployment, bad variable name)
router.post('/login', function(req,res){
  console.log('password');
  console.log('usertype ' + typeof req.body.userName + ' passtype ' 
    + typeof req.body.password);
  myNewModel.loginAttempt(JSON.stringify(req.body.userName), 
    JSON.stringify(req.body.password)).then(function(data){
    console.log(JSON.stringify(data));
    if(data === null){
      res.send(false);
      console.log("---false---");
    }else{
      if(data.typeOfEmployment == "company"){
        var json = {companyName: data.companyName, userType: data.typeOfEmployment};
        res.json(json);
      }else{
        res.json({userType: data.typeOfEmployment});
      }
      
      console.log("type of user: " + data.typeOfEmployment);
      console.log("---true---");
    }
  });
});

//Registers a user, requires: password (hashed), username, userType(cleaner, company), companyName (NULL if cleaner)
router.post('/register', function(req,res){
  console.log("User: " + req.body.userName+ " password: " + req.body.password + " type:"
  + req.body.userType);
  myNewModel.findExistingUser(JSON.stringify(req.body.userName)).then(function(data){
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

  myNewModel.registerUser(JSON.stringify(req.body.userName), JSON.stringify(req.body.password),
   JSON.stringify(req.body.userType), req.body.companyName);

});

//Adds a new deal, requires: price, city, hours(estimated time) and a description
router.post('/addDeal', function(req,res){
  var price = req.body.price;
  var city = req.body.city;
  var time = req.body.hours;
  var description = req.body.description;
  myNewModel.createDeal(price, city, time, description);
  console.log("Deal added");
  res.send("Deal added");
});

//This will return all of your accepted deals, requires: username
router.get('/mytasks', function(req,res){
  var username = req.body.userName;
  console.log("mytasks");
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
