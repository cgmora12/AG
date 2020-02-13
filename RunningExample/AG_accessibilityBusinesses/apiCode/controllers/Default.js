'use strict';

var url = require('url');


var Default = require('./DefaultService');


module.exports.getCategory = function getCategory (req, res, next) {
  Default.getCategory(req.swagger.params, res, next);
};

module.exports.getClosed_Moved = function getClosed_Moved (req, res, next) {
  Default.getClosed_Moved(req.swagger.params, res, next);
};

module.exports.getDecal_Recipient = function getDecal_Recipient (req, res, next) {
  Default.getDecal_Recipient(req.swagger.params, res, next);
};

module.exports.getLocation = function getLocation (req, res, next) {
  Default.getLocation(req.swagger.params, res, next);
};

module.exports.getSubminusCategory = function getSubminusCategory (req, res, next) {
  Default.getSubminusCategory(req.swagger.params, res, next);
};

module.exports.getYear = function getYear (req, res, next) {
  Default.getYear(req.swagger.params, res, next);
};

module.exports.getaccessibilityBusinesses = function getaccessibilityBusinesses (req, res, next) {
  Default.getaccessibilityBusinesses(req.swagger.params, res, next);
};

module.exports.getvisualisation = function getvisualisation (req, res, next) {
  Default.getvisualisation(req.swagger.params, res, next);
};
