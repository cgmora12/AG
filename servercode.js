'use strict';
var fs = require('fs');
var papa = require('papaparse');

exports.getOperation = function(args, res, next) {

  var examples = {};
  var fileName = "./filename.csv";

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
                result = "{ \"csv\": " + JSON.stringify(result) + " }";
                var jsonObj = JSON.parse(result);
                var jsonResult = new Object();
                jsonResult.csv = [];

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
                        if(jsonObj.csv[i][j] === args[Object.keys(args)[j]].value + ""){
                          jsonResult.csv[object_number] = jsonObj.csv[i];
                          object_number++;
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