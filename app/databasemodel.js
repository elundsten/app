var Sequelize = require('sequelize');
//mysql --host=mysql-vt2018.csc.kth.se --user=erlunds_admin --password=BO2V723pq6x5
var sequelize = new Sequelize('schema', 'user', 'password', {
  host: 'localhost',
  dialect: 'mysql',
  port: 3306,
  logging: false,

  dialectOption: {
    multipleStatement: true
  },
  omitNull : true,
  pool: {
    max: 2,
    min: 0,
    idle: 1000
  },
});

var io;

exports.init = function (ioIn){
  io = ioIn;

}


var agreement = sequelize.define("agreements", {

  agreementID: {
    type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },

    price: {
      type: Sequelize.FLOAT
    },

    city: {
      type: Sequelize.STRING
    },

    hours: {
      type: Sequelize.INTEGER
    },

    //vet icke om boolean finns
    accepted: {
      type: Sequelize.BOOLEAN
    },

    done: {
      type: Sequelize.BOOLEAN
    },

    description: {
      type: Sequelize.STRING
    },

    companyName: {
      type: Sequelize.STRING
    }

});


var timeReport = sequelize.define("timereports",{

  userName: {
    type: Sequelize.STRING,
  },

  agreementID: {
    type: Sequelize.INTEGER,
    primaryKey: true
  }, 

  dt: {
    //buy or sell
    type: Sequelize.DATE,
  }, 

  hours: {
    type: Sequelize.INTEGER,
  }
});


var employeeInfo = sequelize.define("employeeInfos",{

  userName: {
    type: Sequelize.STRING,
    primaryKey: true
  },

  realName: {
    type: Sequelize.FLOAT
  },

  city: {
    type: Sequelize.INTEGER
  },

  salary: {
    type: Sequelize.FLOAT
  }

});


var login = sequelize.define("logins",{

  userName: {
    type: Sequelize.STRING,
    primaryKey: true
  },

  password: {
    type: Sequelize.STRING
  },

  typeOfEmployment: {
    type: Sequelize.STRING
  },
  
  companyName: {
      type: Sequelize.STRING
    }

});


var ongoingassignments = sequelize.define("ongoingassignments", {

  agreementID: {
    type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },

    price: {
      type: Sequelize.INTEGER
    },

    city: {
      type: Sequelize.STRING
    },

    hours: {
      type: Sequelize.INTEGER
    },

    userName: {
      type: Sequelize.STRING
    },
    //vet icke om boolean finns
    activeAssignment: {
      type: Sequelize.BOOLEAN
    },

    description: {
      type: Sequelize.STRING
    },

    companyName: {
      type: Sequelize.STRING
    }

});


//When a new deal is created it will add AND then send to everyone
//listening to "onNewDeal" and the table should be updated for the user.
exports.createDeal = function(totalPrice, cityName, totalTime, descript,nameOfCompany){
  console.log(totalTime + " type of: " + typeof totalTime);
  var totalTime = parseInt(totalTime);
  sequelize.sync()
      .then(() => agreement.create({
        price: totalPrice,
        city: cityName,
        hours: totalTime,
        description: descript,
        accepted: false,
        companyName: nameOfCompany,
        done: false
      }).then(task => {
        console.log(JSON.stringify(task));
        io.emit("onNewDeal", task);
      }));
  
}

//total time worked!
exports.setTimeWorked = function(employeeName, assignID,hoursWorked){
  timeReport.findOne({
    where: { agreementID: assignID}
  }).then(function(data){
    if(data == null){
      sequelize.sync()
      .then(() => timeReport.create({
        userName: employeeName,
        agreementID: assignID,
        hours: hoursWorked
      }).then(() =>{
        ongoingassignments.update({
          hours: sequelize.literal('hours +' + hoursWorked)
        },
        {
          where:{agreementID: assignID}
        })
      }));
    }else{
      //TODO: update time if table input exists...
      timeReport.update({
        hours: sequelize.literal('hours + ' + hoursWorked)
      },
      {
        where:{agreementID: assignID}
      });
      ongoingassignments.update({
          hours: sequelize.literal('hours +' + hoursWorked)
        },
        {
          where:{agreementID: assignID}
        });
    }

  });

}
exports.getSalt(user).then(function(data){
  var salt = data.salt;
})

exports.getSalt = function(user){
  sequelize.sync()
  .then(() =>login.findAll({
    where: {userName: user}
  }).then(function(data){
    console.log(data);
    return data
  }));
}

exports.getSalt = function(user){
  return login.findAll({
    where:{
      userName: user
    }
  }).then(function(data){
    return data;
  });
}

exports.registerUser = function(user, hashedPassword, userType) {
  sequelize.sync()
  .then(() => login.create({
    userName: user,
    password: hashedPassword,
    typeOfEmployment: userType
  })).catch(function(err) {
    // print the error details
    console.log("=========================");
    console.log("User already exists");
    console.log("=========================");
  });

}


exports.findExistingUser = function(user){
  return login.findOne({
    where: {
      userName: user
    }
  }).then(function(data){
    return data;
  });
}


exports.loginAttempt = function(user,hashedPassword){
  return login.findOne({
    where: {
      userName: user,
      password: hashedPassword
    }
  }).then(function(data){
    return data;
  });
}

exports.createOngoingassignment = function(assignID, cityName, 
  user, descript, nameOfCompany){
  console.log("===========1===========");
  ongoingassignments.create({
    agreementID: assignID,
    price: "0",
    city: cityName,
    userName: user,
    hours: "0",
    activeAssignment: true,
    companyName: nameOfCompany,
    description: descript
  }).catch(function(err) {
    // print the error details
    console.log("ERROR: Trying to add agreementID which already exists");
});
}

//returns deal with all the information
//NOTE: deals that are not accepted
exports.getDeals = function(){
  return agreements.findAll({
    where:{
      accepted: false
    }
  }).then(function(data){
    return data;
  });
}

//Updating real time so everyone can see that this assignment is taken.
//Using websockets, everyone listening to "updateExistingDeal".
//return {agreementID: theActualID, accepted: true}
//Note a company also listen to this and see if their deal is taken.
exports.accpetDeal = function(assignID){
  agreement.update({
    accepted: true
  },
  {
    where:{ 
      agreementID: assignID,
      accepted: false
    }
  }).then(task=> {
    console.log("===============");
    console.log(task[0]);
    console.log("===============");
    if (task[0] != 0){
      var dealUpdate = {agreementID: assignID, accepted: true};
      io.emit("updateExistingDeal", dealUpdate);
    }else{
      console.log("task value: " + task[0] );
    }
  }).catch(function(err) {
    // print the error details
    console.log(err, "Failing in acceptDeal in databasemode.js");
});
}

exports.getMyAssignments = function(user){
  return ongoingassignments.findAll({
    where:{
      userName: user
    }
  }).then(function(data){
    return data;
  });
}

exports.companyGetDeals =function(nameOfCompany){
  return agreement.findAll({
    where:{
      companyName: nameOfCompany
    }
  }).then(function(data){
    return data;
  });
}

exports.companyGetOngoingAssignments = function(nameOfCompany){
    return ongoingassignments.findAll({
      where:{
        companyName:nameOfCompany,
        activeAssignment: true
      }
    }).then(function(data){
      return data;
    });
}