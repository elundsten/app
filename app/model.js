/* jslint node: true */
"use strict";

/**
 * A module that contains the main system object!
 * @module roomSystem
 */

var roomList = [];


/**
 * Creates a room with the given name.
 * @param {String} name - The name of the room.
 */
function Room(name) {
    this.name = name;
    this.messages = [];
    this.users = [];

    this.addMessage = function(message){
      this.messages.push(message);
    };
}


/**
 * Creates a room with the given name.
 * @param {String} name - The name of the room.
 */
exports.addRoom = function (name) {
  var newRoom = new Room(name);
  roomList.push(newRoom);
};

/**
 * Returns all the Rooms.
 */
exports.getRooms = function() {
  return roomList;
};

/**
 * Removes the room object with the matching name.
 * @param {String} name - The name of the room.
 */
exports.removeRoom = function(name){
  for (var i = 0; i < roomList.length; i++) {
    var room = roomList[i];
    if (room.name === name) {
      roomList.splice(i, 1);
      room.remove();
      break;
    }
  }
};

/**
 * Return the room object with the matching name.
 * @param {String} name - The name of the room.
 */
exports.findRoom = function(name) {
  for (var i = 0; i < roomList.length; i++) {
    if (roomList[i].name === name) {
      return roomList[i];
    }
  }
};
