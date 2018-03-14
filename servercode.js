'use strict';
var toJson = require('allto-json');
var fs = require('fs');

exports.getListadodecentrosdelaUA = function(args, res, next) {

  var examples = {};
  var fileName = "./Listado de centros de la UA.csv";

  if (fs.existsSync(fileName)) {
    console.log('file exists');
    // From csv to json
    toJson.csv(fileName, function(error, result){
      try{
        // Creating json object
        result = "{ \"csv\": " + JSON.stringify(result) + " }";
        var jsonObj = JSON.parse(result);
        var jsonResult = new Object();
        jsonResult.csv = [];
        console.log(jsonObj);
        //console.log(JSON.stringify(args));

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
          jsonResult.csv = jsonObj.csv;
        }
        else {
          for(var j = 0; j < Object.keys(args).length; j++){
            if(args[Object.keys(args)[j]].value != undefined){
              if(jsonResult.csv.length > 0 || filters){
                jsonObj.csv = jsonResult.csv;
                jsonResult.csv = [];
                object_number = 0;
              }
              filters = true;
              for(var i in jsonObj.csv){
                if(jsonObj.csv[i][j] === args[Object.keys(args)[j]].value){
                  jsonResult.csv[object_number] = jsonObj.csv[i];
                  object_number++;
                }
              }
            } 
          }
        }

        console.log(jsonResult);
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
    });
  } 
  else {
        res.end();
  }
}