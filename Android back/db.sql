use cleaningcompany;

drop table `agreements`;
drop table `timereports`;
drop table `employeeinfos`;
drop table `logins`;
drop table `ongoingassignments`;


create table agreements (
  agreementID int(11) NOT NULL AUTO_INCREMENT,
  price float DEFAULT NULL,
  city varchar(255) DEFAULT NULL,
  hours int(11) DEFAULT NULL,
  accepted tinyint(1) DEFAULT NULL,
  done tinyint(1) DEFAULT NULL,
  createdAt datetime NOT NULL,
  updatedAt datetime NOT NULL,
  description varchar(255) DEFAULT NULL,
  companyName varchar(255) DEFAULT NULL,
  PRIMARY KEY (agreementID)
); 

create table timereports (
  userName varchar(255) NOT NULL,
  agreementID int(11) DEFAULT NULL,
  dt datetime DEFAULT NULL,
  hours int(11) DEFAULT NULL,
  createdAt datetime NOT NULL,
  updatedAt datetime NOT NULL,
  PRIMARY KEY (userName)
);

create table employeeinfos (
  userName varchar(255) NOT NULL,
  realName float DEFAULT NULL,
  city int(11) DEFAULT NULL,
  salary float DEFAULT NULL,
  createdAt datetime NOT NULL,
  updatedAt datetime NOT NULL,
  PRIMARY KEY (userName)
);

create table logins (
  userName varchar(255) NOT NULL,
  password varchar(255) DEFAULT NULL,
  typeOfEmployment varchar(255) DEFAULT NULL,
  companyName varchar(255) DEFAULT NULL,
  createdAt datetime NOT NULL,
  updatedAt datetime NOT NULL,
  PRIMARY KEY (userName)
);

create table ongoingassignments (
  agreementID varchar(64) NOT NULL,
  price float DEFAULT NULL,
  city varchar(64) DEFAULT NULL,
  hours int(11) DEFAULT NULL,
  userName varchar(64) DEFAULT NULL,
  activeAssignment tinyint(1) DEFAULT NULL,
  createdAt datetime DEFAULT NULL,
  updatedAt datetime DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  companyName varchar(255) DEFAULT NULL,
  PRIMARY KEY (agreementID)
);