'use strict';
var fs = require('fs');
var papa = require('papaparse');

exports.getOperation = function(args, queryArgs, res, next) {

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
                result = "{ \"csv\": " + JSON.stringify(result) + " }";
                var jsonObj = JSON.parse(result);
                var jsonResult = new Object();
                jsonResult.csv = [];

                // Filtering results
                var argsUndefined = true;
                var filters = false;
                var object_number = 0;

                for(var i = 0; i < Object.keys(queryArgs).length; i++){
                  if(queryArgs[Object.keys(queryArgs)[i]] != undefined){
                    argsUndefined = false;
                  }
                }
                if(argsUndefined){
                  jsonResult.csv = jsonObj.csv;
                }
                else {
                  for(var j = 0; j < Object.keys(queryArgs).length; j++){
                    if(queryArgs[Object.keys(queryArgs)[j]] != undefined){
                      if(jsonResult.csv.length > 0 || filters){
                        jsonObj.csv = jsonResult.csv;
                        jsonResult.csv = [];
                        object_number = 0;
                      }
                      filters = true;
                      for(var i in jsonObj.csv){
                        if(Object.keys(queryArgs)[j] === Object.keys(jsonObj.csv[i])[Object.keys(jsonObj.csv[i]).indexOf(Object.keys(queryArgs)[j])]){
                          if(jsonObj.csv[i][Object.keys(jsonObj.csv[i])[Object.keys(jsonObj.csv[i]).indexOf(Object.keys(queryArgs)[j])]] === queryArgs[Object.keys(queryArgs)[j]] + ""){
                            jsonResult.csv[object_number] = jsonObj.csv[i];
                            object_number++;
                          }
                          else if(queryArgs[Object.keys(queryArgs)[j]] + "" === "all" && jsonObj.csv[i][Object.keys(jsonObj.csv[i])[Object.keys(jsonObj.csv[i]).indexOf(Object.keys(queryArgs)[j])]].replace(/\s+/g, '') != ""){
                            jsonResult.csv[object_number] = jsonObj.csv[i];
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