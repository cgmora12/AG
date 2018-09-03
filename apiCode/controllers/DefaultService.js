'use strict';
var fs = require('fs');
var papa = require('papaparse');

exports.getOperation = function(args, res, next) {
  
  var examples = {};
  var fileName = "./data.csv";

  if (fs.existsSync(fileName)) {
    console.log('file exists');

    fs.readFile(fileName, {encoding: 'utf-8'}, function(err,data){
      if (!err) {
        // From csv to json
        papa.parse(data, 
          { 
            header: true,
            complete: function ReturnJSONFile(result){
              console.log('ReturnJSONFile');
              result = result.data;
              try{
                // Creating json object
                result = "{ \"results\": " + JSON.stringify(result) + " }";
                var jsonObj = JSON.parse(result);
                var jsonResult = new Object();
                jsonResult.results = [];

                // Filtering results
                var argsUndefined = true;
                var filters = false;
                var object_number = 0;

                for(var i = 0; i < Object.keys(args).length; i++){
                  if(args[Object.keys(args)[i]].value != undefined){
                    argsUndefined = false;
                  }
                }
                if(argsUndefined){
                  jsonResult.results = jsonObj.results;
                }
                else {
                  for(var j = 0; j < Object.keys(args).length; j++){
                    if(args[Object.keys(args)[j]].value != undefined){
                      if(jsonResult.results.length > 0 || filters){
                        jsonObj.results = jsonResult.results;
                        jsonResult.results = [];
                        object_number = 0;
                      }
                      filters = true;
                      for(var i in jsonObj.results){
                        if(Object.keys(args)[j] === Object.keys(jsonObj.results[i])[Object.keys(jsonObj.results[i]).indexOf(Object.keys(args)[j])]){
                          if(jsonObj.results[i][Object.keys(jsonObj.results[i])[Object.keys(jsonObj.results[i]).indexOf(Object.keys(args)[j])]] === args[Object.keys(args)[j]].value + ""){
                            jsonResult.results[object_number] = jsonObj.results[i];
                            object_number++;
                          }
                          else if(args[Object.keys(args)[j]].value + "" === "all" && jsonObj.results[i][Object.keys(jsonObj.results[i])[Object.keys(jsonObj.results[i]).indexOf(Object.keys(args)[j])]].replace(/\s+/g, '') != ""){
                            jsonResult.results[object_number] = jsonObj.results[i];
                            object_number++;
                          }
                        }
                      }
                    } 
                  }
                }

                console.log("Completed!");
                examples['application/json'] = jsonResult;
              } catch (err) {
                  console.log(err);
              }

              if(Object.keys(examples).length > 0) {
                res.setHeader('Content-Type', 'application/json');
                res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
              }
              else {
                res.end();
              }
            },
            error: function(err){
              console.log(err);
              res.end();
            }       
          }
        );
      } else {
          console.log(err);
      }
    });
  } 
  else {
        res.end();
  }
}
exports.getabout = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.getdata = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.gethasEscalator = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.gethasLift = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.gethasTravelator = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.getlineAbout = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.getlineID = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.getlineName = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.getrouteService = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.getstationName = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
exports.gettransfer = function(args, res, next) {
	exports.getOperation(args, res, next); 
}
