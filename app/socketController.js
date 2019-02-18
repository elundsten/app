/* jslint node: true */
"use strict";
var databasemodel = require('./databasemodel.js')

module.exports = function (socket, io) {

  // user joins room
  socket.on('join', function (req) {
    console.log("A new user joined!");
    console.log(req);
  });

  // user gets updated
  socket.on('update', function (req) {
  });

  // user leaves room
  socket.on('leave', function (req) {

  });
socket.on('getDeals', function(req){

});

//requires username and assignmentID, city, description
//will add a new row of ongoingassigment.
//will send via websocket to "updateExistingDeal"
  socket.on('acceptDeal',function(req){
    console.log("Accpeting deal...");
    console.log(JSON.stringify(req));
    databasemodel.accpetDeal(req.agreementID);
    //HÄR SLUTADE DU, fortsätt med att fixa ongoingassignment!
    //detta för att setTimeworked ska funka.
    databasemodel.createOngoingassignment(req.agreementID, req.city,req.userName, 
      req.description, req.companyName);
  });

  //Cleaners update how long it took for a particular assignment
  //Requires: assignmentID, userName, hours (worked)
  socket.on('timeUpdate', function(req){
    console.log("Adding to time report...");
    databasemodel.setTimeWorked( req.userName,req.agreementID, req.hours);

  });
};