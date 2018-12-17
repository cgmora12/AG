'use strict';

var url = require('url');


var Default = require('./DefaultService');


module.exports.getlocation_type = function getlocation_type (req, res, next) {
  Default.getlocation_type(req.swagger.params, res, next);
};

module.exports.getparent_station = function getparent_station (req, res, next) {
  Default.getparent_station(req.swagger.params, res, next);
};

module.exports.getstop_code = function getstop_code (req, res, next) {
  Default.getstop_code(req.swagger.params, res, next);
};

module.exports.getstop_desc = function getstop_desc (req, res, next) {
  Default.getstop_desc(req.swagger.params, res, next);
};

module.exports.getstop_id = function getstop_id (req, res, next) {
  Default.getstop_id(req.swagger.params, res, next);
};

module.exports.getstop_lat = function getstop_lat (req, res, next) {
  Default.getstop_lat(req.swagger.params, res, next);
};

module.exports.getstop_lon = function getstop_lon (req, res, next) {
  Default.getstop_lon(req.swagger.params, res, next);
};

module.exports.getstop_name = function getstop_name (req, res, next) {
  Default.getstop_name(req.swagger.params, res, next);
};

module.exports.getstop_timezone = function getstop_timezone (req, res, next) {
  Default.getstop_timezone(req.swagger.params, res, next);
};

module.exports.getstop_url = function getstop_url (req, res, next) {
  Default.getstop_url(req.swagger.params, res, next);
};

module.exports.getstopsmetro = function getstopsmetro (req, res, next) {
  Default.getstopsmetro(req.swagger.params, res, next);
};

module.exports.getvisualization = function getvisualization (req, res, next) {
  Default.getvisualization(req.swagger.params, res, next);
};

module.exports.getwheelchair_boarding = function getwheelchair_boarding (req, res, next) {
  Default.getwheelchair_boarding(req.swagger.params, res, next);
};

module.exports.getzone_id = function getzone_id (req, res, next) {
  Default.getzone_id(req.swagger.params, res, next);
};
