'use strict';

var url = require('url');


var Default = require('./DefaultService');


module.exports.getabout = function getabout (req, res, next) {
  Default.getabout(req.swagger.params, res, next);
};

module.exports.getdata = function getdata (req, res, next) {
  Default.getdata(req.swagger.params, res, next);
};

module.exports.gethasEscalator = function gethasEscalator (req, res, next) {
  Default.gethasEscalator(req.swagger.params, res, next);
};

module.exports.gethasLift = function gethasLift (req, res, next) {
  Default.gethasLift(req.swagger.params, res, next);
};

module.exports.gethasTravelator = function gethasTravelator (req, res, next) {
  Default.gethasTravelator(req.swagger.params, res, next);
};

module.exports.getlineAbout = function getlineAbout (req, res, next) {
  Default.getlineAbout(req.swagger.params, res, next);
};

module.exports.getlineID = function getlineID (req, res, next) {
  Default.getlineID(req.swagger.params, res, next);
};

module.exports.getlineName = function getlineName (req, res, next) {
  Default.getlineName(req.swagger.params, res, next);
};

module.exports.getrouteService = function getrouteService (req, res, next) {
  Default.getrouteService(req.swagger.params, res, next);
};

module.exports.getstationName = function getstationName (req, res, next) {
  Default.getstationName(req.swagger.params, res, next);
};

module.exports.gettransfer = function gettransfer (req, res, next) {
  Default.gettransfer(req.swagger.params, res, next);
};
