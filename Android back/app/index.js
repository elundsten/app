var express = require('express');
var bodyParser = require('body-parser');
var http = require('http');
var expressSession = require('express-session');
var sharedsession = require('express-socket.io-session');

var port = 8080;

var app = express();
app.get('/', function(req, res) {
  console.log("connected 123123123");
  res.status(401).send("String");
});

app.get('/somethingelse', function(req,res){
  console.log("GET method works!");
  res.status(401).send("String");
});

app.post('/something', function(req,res){
  console.log("it's working");
  res.send("hello");
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

var session = expressSession({
    secret: "MoveFromHereOrTheSecretWillBeOnGit",
    resave: true,
    saveUninitialized: true,
  });

app.use(session);
var httpServer = http.Server(app);
var io = require('socket.io').listen(httpServer);
io.use(sharedsession(session));

var router = require('./controller.js');
app.use('/API', router);

var socketController = require('./socketController.js');
io.on('connection', function (socket) {
  console.log("connected");
  socketController(socket, io);
});

var databasemodel = require("./databasemodel.js");
databasemodel.init(io);

httpServer.listen(port, function () {
  console.log("server listening on port", port);
});

