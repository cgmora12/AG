'use strict';

var url = require('url');


var Default = require('./DefaultService');


module.exports.about = function about (req, res, next) {
  Default.about(req.swagger.params, res, next);
};

module.exports.getdata = function getdata (req, res, next) {
  Default.getdata(req.swagger.params, res, next);
};

module.exports.hasEscalator = function hasEscalator (req, res, next) {
  Default.hasEscalator(req.swagger.params, res, next);
};

module.exports.hasLift = function hasLift (req, res, next) {
  Default.hasLift(req.swagger.params, res, next);
};

module.exports.hasTravelator = function hasTravelator (req, res, next) {
  Default.hasTravelator(req.swagger.params, res, next);
};

module.exports.lineAbout = function lineAbout (req, res, next) {
  Default.lineAbout(req.swagger.params, res, next);
};

module.exports.lineID = function lineID (req, res, next) {
  Default.lineID(req.swagger.params, res, next);
};

module.exports.lineName = function lineName (req, res, next) {
  Default.lineName(req.swagger.params, res, next);
};

module.exports.routeService = function routeService (req, res, next) {
  Default.routeService(req.swagger.params, res, next);
};

module.exports.stationName = function stationName (req, res, next) {
  Default.stationName(req.swagger.params, res, next);
};

module.exports.transfer = function transfer (req, res, next) {
  Default.transfer(req.swagger.params, res, next);
};
